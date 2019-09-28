package com.caychen.micro.xunwu.service.impl;

import com.caychen.micro.xunwu.bo.HouseSort;
import com.caychen.micro.xunwu.dto.HouseDetailDto;
import com.caychen.micro.xunwu.dto.HouseDto;
import com.caychen.micro.xunwu.dto.HousePictureDto;
import com.caychen.micro.xunwu.entity.*;
import com.caychen.micro.xunwu.enums.HouseStatusEnum;
import com.caychen.micro.xunwu.form.DatatableSearch;
import com.caychen.micro.xunwu.form.HouseForm;
import com.caychen.micro.xunwu.form.PhotoForm;
import com.caychen.micro.xunwu.form.RentSearch;
import com.caychen.micro.xunwu.global.ServiceMultiResult;
import com.caychen.micro.xunwu.global.ServiceResult;
import com.caychen.micro.xunwu.repository.*;
import com.caychen.micro.xunwu.service.IHouseService;
import com.caychen.micro.xunwu.service.IQiNiuService;
import com.caychen.micro.xunwu.util.LoginUserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Service
@Slf4j
public class HouseServiceImpl implements IHouseService {

	@Value("${cdn.prefix}")
	private String cdnPrefix;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IHouseRepository houseRepository;

	@Autowired
	private IHouseDetailRepository houseDetailRepository;

	@Autowired
	private IHousePictureRepository housePictureRepository;

	@Autowired
	private IHouseTagRepository houseTagRepository;

	@Autowired
	private ISubwayRepository subwayRepository;

	@Autowired
	private ISubwayStationRepository subwayStationRepository;

	@Autowired
	private IHouseSubscribeRespository subscribeRespository;

	@Autowired
	private IQiNiuService qiNiuService;


	@Override
	@Transactional
	public ServiceResult<HouseDto> save(HouseForm houseForm) {
		HouseDetail houseDetail = new HouseDetail();
		ServiceResult<HouseDto> subwayValidateResult = this.wrapperDetailInfo(houseDetail, houseForm);

		if (subwayValidateResult != null) {
			return subwayValidateResult;
		}

		House house = modelMapper.map(houseForm, House.class);
		house.setCreateTime(new Date());
		house.setLastUpdateTime(new Date());
		house.setAdminId(LoginUserUtil.getUserId());
		//保存房屋信息
		house = houseRepository.save(house);

		houseDetail.setHouseId(house.getId());
		//详细信息
		houseDetail = houseDetailRepository.save(houseDetail);

		List<HousePicture> housePictureList = this.generatePictures(houseForm, house.getId());
		Iterable<HousePicture> pictures = null;
		if (!CollectionUtils.isEmpty(housePictureList)) {
			//房屋图片
			pictures = housePictureRepository.saveAll(housePictureList);
		}

		HouseDto houseDto = modelMapper.map(house, HouseDto.class);
		HouseDetailDto houseDetailDto = modelMapper.map(houseDetail, HouseDetailDto.class);
		houseDto.setHouseDetail(houseDetailDto);

		if (pictures != null) {
			List<HousePictureDto> housePictureDtoList =
					Lists.newArrayList(pictures).stream().map(p -> modelMapper.map(p, HousePictureDto.class)).collect(Collectors.toList());
			houseDto.setPictures(housePictureDtoList);

			houseDto.setCover(this.cdnPrefix + houseDto.getCover());
		}

		List<String> tags = houseForm.getTags();
		final Integer houseId = house.getId();
		if (!CollectionUtils.isEmpty(tags)) {
			List<HouseTag> houseTagList = tags.stream().map(tag -> new HouseTag(houseId, tag)).collect(Collectors.toList());

			houseTagRepository.saveAll(houseTagList);

			houseDto.setTags(tags);
		}

		return new ServiceResult<>(true, null, houseDto);
	}

	@Override
	public ServiceMultiResult<HouseDto> adminQuery(DatatableSearch searchBody) {
		//分页
		Sort sort = new Sort(Sort.Direction.fromString(searchBody.getDirection()), searchBody.getOrderBy());
		int page = searchBody.getStart() / searchBody.getLength();
		Pageable pageable = PageRequest.of(page, searchBody.getLength(), sort);

		Specification<House> specification = (Specification<House>) (root, query, cb) -> {
			//1、只能看到自己创建的房源信息
			Predicate predicate =
					cb.equal(root.get("adminId"), LoginUserUtil.getUserId());
			//2、不能看到已删除的房源
			predicate = cb.and(predicate, cb.notEqual(root.get("status"), HouseStatusEnum.DELETED.getValue()));

			if (StringUtils.hasText(searchBody.getCity())) {
				predicate = cb.and(predicate, cb.equal(root.get("cityEnName"), searchBody.getCity()));
			}
			if (searchBody.getStatus() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("status"), searchBody.getStatus()));
			}

			if (searchBody.getCreateTimeMin() != null) {
				predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMin()));
			}

			if (searchBody.getCreateTimeMax() != null) {
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMax()));
			}

			if (StringUtils.hasText(searchBody.getTitle())) {
				predicate = cb.and(predicate, cb.like(root.get("title"), "%" + searchBody.getTitle() + "%"));
			}

			return predicate;
		};

		Page<House> houses = houseRepository.findAll(specification, pageable);

		List<HouseDto> houseDtoList = Lists.newArrayList(houses).stream().map(house -> {
			HouseDto houseDto = modelMapper.map(house, HouseDto.class);
			houseDto.setCover(this.cdnPrefix + house.getCover());
			return houseDto;
		}).collect(Collectors.toList());

		int totalElements = (int) houses.getTotalElements();

		return new ServiceMultiResult<>(totalElements, houseDtoList);
	}

	@Override
	public ServiceResult<HouseDto> findCompleteOne(Integer id) {
		Optional<House> optionalHouse = houseRepository.findById(id);
		House house = optionalHouse.orElse(null);
		if (house == null) {
			return ServiceResult.notFound();
		}

		//详情
		HouseDetail detail = houseDetailRepository.findByHouseId(id);
		//房源图片
		List<HousePicture> pictures = housePictureRepository.findAllByHouseId(id);
		HouseDetailDto detailDTO = modelMapper.map(detail, HouseDetailDto.class);
		List<HousePictureDto> pictureDTOS =
				pictures.stream()
						.map(picture -> modelMapper.map(picture, HousePictureDto.class)).collect(Collectors.toList());

		//房源标签
		List<HouseTag> tags = houseTagRepository.findAllByHouseId(id);
		List<String> tagList = tags.stream().map(HouseTag::getName).collect(Collectors.toList());

		HouseDto result = modelMapper.map(house, HouseDto.class);
		result.setHouseDetail(detailDTO);
		result.setPictures(pictureDTOS);
		result.setTags(tagList);

/*
		if (LoginUserUtil.getUserId() > 0) { // 已登录用户
			HouseSubscribe subscribe = subscribeRespository.findByHouseIdAndUserId(house.getId(), LoginUserUtil.getUserId());
			if (subscribe != null) {
				result.setSubscribeStatus(subscribe.getStatus());
			}
		}
*/

		return ServiceResult.success(result);
	}

	@Override
	@Transactional
	public ServiceResult update(HouseForm houseForm) {
		Optional<House> optionalHouse = this.houseRepository.findById(houseForm.getId());
		if (optionalHouse == null || !optionalHouse.isPresent()) {
			return ServiceResult.notFound();
		}

		House house = optionalHouse.get();

		HouseDetail detail = this.houseDetailRepository.findByHouseId(house.getId());
		if (detail == null) {
			return ServiceResult.notFound();
		}

		ServiceResult wrapperResult = wrapperDetailInfo(detail, houseForm);
		if (wrapperResult != null) {
			return wrapperResult;
		}

		houseDetailRepository.save(detail);

		List<HousePicture> pictures = generatePictures(houseForm, houseForm.getId());
		housePictureRepository.saveAll(pictures);

		if (houseForm.getCover() == null) {
			houseForm.setCover(house.getCover());
		}

		modelMapper.map(houseForm, house);
		//更新时间
		house.setLastUpdateTime(new Date());
		houseRepository.save(house);

		//todo
		if (house.getStatus() == HouseStatusEnum.PASSED.getValue()) {
//			searchService.index(house.getId());
		}

		return ServiceResult.success();
	}

	@Override
	@Transactional
	public ServiceResult removePhoto(Integer id) {
		Optional<HousePicture> optionalHousePicture = housePictureRepository.findById(id);
		if (optionalHousePicture == null || !optionalHousePicture.isPresent()) {
			return ServiceResult.notFound();
		}

		try {
			Response response = this.qiNiuService.delete(optionalHousePicture.get().getPath());
			if (response.isOK()) {
				housePictureRepository.deleteById(id);
				return ServiceResult.success();
			} else {
				return new ServiceResult(false, response.error);
			}
		} catch (QiniuException e) {
//			e.printStackTrace();
			log.error("删除图片: ", e);
			return new ServiceResult(false, e.getMessage());
		}
	}

	@Override
	public ServiceResult updateCover(Integer coverId, Integer targetId) {
		Optional<HousePicture> optionalHousePicture = housePictureRepository.findById(coverId);
		if (optionalHousePicture == null || !optionalHousePicture.isPresent()) {
			return ServiceResult.notFound();
		}

		HousePicture cover = optionalHousePicture.get();

		houseRepository.updateCover(targetId, cover.getPath());
		return ServiceResult.success(null);
	}

	@Override
	@Transactional
	public ServiceResult addTag(Integer houseId, String tag) {
		Optional<House> optionalHouse = houseRepository.findById(houseId);
		if (optionalHouse == null || !optionalHouse.isPresent()) {
			return ServiceResult.notFound();
		}

		HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
		if (houseTag != null) {
			return new ServiceResult(false, "标签已存在");
		}

		houseTagRepository.save(new HouseTag(houseId, tag));
		return ServiceResult.success();
	}

	@Override
	@Transactional
	public ServiceResult removeTag(Integer houseId, String tag) {
		Optional<House> optionalHouse = houseRepository.findById(houseId);
		if (optionalHouse == null || !optionalHouse.isPresent()) {
			return ServiceResult.notFound();
		}

		HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
		if (houseTag == null) {
			return new ServiceResult(false, "标签不存在");
		}

		houseTagRepository.deleteById(houseTag.getId());
		return ServiceResult.success();
	}

	@Override
	@Transactional
	public ServiceResult updateStatus(Integer id, int status) {
		Optional<House> optionalHouse = houseRepository.findById(id);
		if (optionalHouse == null || !optionalHouse.isPresent()) {
			return ServiceResult.notFound();
		}

		House house = optionalHouse.get();

		if (house.getStatus() == status) {
			return new ServiceResult(false, "状态没有发生变化");
		}

		if (house.getStatus() == HouseStatusEnum.RENTED.getValue()) {
			return new ServiceResult(false, "已出租的房源不允许修改状态");
		}

		if (house.getStatus() == HouseStatusEnum.DELETED.getValue()) {
			return new ServiceResult(false, "已删除的资源不允许操作");
		}

		houseRepository.updateStatus(id, status);

		// 上架更新索引 其他情况都要删除索引
		if (status == HouseStatusEnum.PASSED.getValue()) {
//			searchService.index(id);
		} else {
//			searchService.remove(id);
		}
		return ServiceResult.success();
	}

	@Override
	public ServiceMultiResult<HouseDto> query(RentSearch rentSearch) {
		Sort sort = HouseSort.generateSort(rentSearch.getOrderBy(), rentSearch.getOrderDirection());
		int page = rentSearch.getStart() / rentSearch.getSize();

		Pageable pageable = PageRequest.of(page, rentSearch.getSize(), sort);

		Specification<House> specification = (Specification<House>) (root, query, cb) -> {
			Predicate predicate = cb.equal(root.get("status"), HouseStatusEnum.PASSED.getValue());

			predicate = cb.and(predicate, cb.equal(root.get("cityEnName"), rentSearch.getCityEnName()));

			//有些距离为-1（默认），如果参与排序，需要将-1的数据过滤掉
			if (org.apache.commons.lang3.StringUtils.equals(HouseSort.DISTANCE_TO_SUBWAY_KEY, rentSearch.getOrderBy())) {
				predicate = cb.and(predicate, cb.gt(root.get(rentSearch.getOrderBy()), -1));
			}

			return predicate;
		};

		Page<House> housePage = houseRepository.findAll(specification, pageable);
		Map<Integer, HouseDto> idToHouseMap = Maps.newHashMap();
		List<Integer> houseIds = Lists.newArrayList();

		List<HouseDto> houseDtoList = housePage.stream().map(house -> {
			HouseDto houseDto = modelMapper.map(house, HouseDto.class);
			houseDto.setCover(this.cdnPrefix + house.getCover());
			idToHouseMap.put(house.getId(), houseDto);
			houseIds.add(house.getId());

			return houseDto;
		}).collect(Collectors.toList());

		wrapperHouseList(houseIds, idToHouseMap);

		int totalElements = (int) housePage.getTotalElements();

		return new ServiceMultiResult<HouseDto>(totalElements, houseDtoList);

		/*if (rentSearch.getKeywords() != null && !rentSearch.getKeywords().isEmpty()) {
			ServiceMultiResult<Long> serviceResult = searchService.query(rentSearch);
			if (serviceResult.getTotal() == 0) {
				return new ServiceMultiResult<>(0, new ArrayList<>());
			}

			return new ServiceMultiResult<>(serviceResult.getTotal(), wrapperHouseResult(serviceResult.getResult()));
		}

		return simpleQuery(rentSearch);*/
	}

	//渲染房源详细信息及标签
	private void wrapperHouseList(List<Integer> ids, Map<Integer, HouseDto> idToHouseMap) {

		//详细信息
		List<HouseDetail> houseDetails = houseDetailRepository.findAllByHouseIdIn(ids);

		houseDetails.stream().forEach(houseDetail -> {
			HouseDto houseDto = idToHouseMap.get(houseDetail.getHouseId());

			HouseDetailDto houseDetailDto = modelMapper.map(houseDetail, HouseDetailDto.class);
			houseDto.setHouseDetail(houseDetailDto);
		});

		//标签
		List<HouseTag> houseTags = houseTagRepository.findAllByHouseIdIn(ids);
		houseTags.stream().forEach(houseTag -> {
			HouseDto houseDto = idToHouseMap.get(houseTag.getHouseId());

			List<String> tags = houseDto.getTags();
			if (tags == null) {
				tags = Lists.newArrayList();
			}
			tags.add(houseTag.getName());
		});
	}

	private ServiceResult<HouseDto> wrapperDetailInfo(HouseDetail houseDetail, HouseForm houseForm) {
		Optional<Subway> subwayOptional = subwayRepository.findById(houseForm.getSubwayLineId());

		if (!subwayOptional.isPresent()) {
			return new ServiceResult<>(false, "Not valid subway line");
		}

		Subway subway = subwayOptional.get();

		Optional<SubwayStation> subwayStationOptional = subwayStationRepository.findById(houseForm.getSubwayStationId());
		if (!subwayStationOptional.isPresent() || subway.getId() != subwayStationOptional.get().getSubwayId()) {
			return new ServiceResult<>(false, "Not valid subway station");
		}

		SubwayStation subwayStation = subwayStationOptional.get();

		houseDetail.setSubwayLineId(subway.getId());
		houseDetail.setSubwayLineName(subway.getName());

		houseDetail.setSubwayStationId(subwayStation.getId());
		houseDetail.setSubwayStationName(subwayStation.getName());

		houseDetail.setDescription(houseForm.getDescription());
		houseDetail.setDetailAddress(houseForm.getDetailAddress());
		houseDetail.setLayoutDesc(houseForm.getLayoutDesc());
		houseDetail.setRentWay(houseForm.getRentWay());
		houseDetail.setRoundService(houseForm.getRoundService());
		houseDetail.setTraffic(houseForm.getTraffic());

		return null;
	}


	private List<HousePicture> generatePictures(HouseForm houseForm, Integer houseId) {
		List<HousePicture> housePictureList = Lists.newArrayList();
		List<PhotoForm> photos = houseForm.getPhotos();
		if (CollectionUtils.isEmpty(photos)) {
			return housePictureList;
		}

		photos.stream().forEach(photo -> {
			HousePicture housePicture = new HousePicture();
			housePicture.setCdnPrefix(cdnPrefix);
			housePicture.setHouseId(houseId);
			housePicture.setPath(photo.getPath());
			housePicture.setWidth(photo.getWidth());
			housePicture.setHeight(photo.getHeight());
			housePictureList.add(housePicture);
		});

		return housePictureList;
	}
}
