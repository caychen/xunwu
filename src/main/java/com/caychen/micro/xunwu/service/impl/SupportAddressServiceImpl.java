package com.caychen.micro.xunwu.service.impl;

import com.caychen.micro.xunwu.dto.SubwayDto;
import com.caychen.micro.xunwu.dto.SubwayStationDto;
import com.caychen.micro.xunwu.dto.SupportAddressDto;
import com.caychen.micro.xunwu.entity.Subway;
import com.caychen.micro.xunwu.entity.SubwayStation;
import com.caychen.micro.xunwu.entity.SupportAddress;
import com.caychen.micro.xunwu.global.ServiceMultiResult;
import com.caychen.micro.xunwu.global.ServiceResult;
import com.caychen.micro.xunwu.repository.ISubwayRepository;
import com.caychen.micro.xunwu.repository.ISubwayStationRepository;
import com.caychen.micro.xunwu.repository.ISupportAddressRepository;
import com.caychen.micro.xunwu.service.ISupportAddressService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
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
public class SupportAddressServiceImpl implements ISupportAddressService {

	@Autowired
	private ISupportAddressRepository supportAddressRepository;

	@Autowired
	private ISubwayRepository subwayRepository;

	@Autowired
	private ISubwayStationRepository subwayStationRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ServiceMultiResult<SupportAddressDto> findAllCities() {
		List<SupportAddress> addressList = supportAddressRepository.findAllByLevel(SupportAddress.Level.CITY.getValue());

		if (CollectionUtils.isEmpty(addressList)) {
			return ServiceMultiResult.empty();
		}

		List<SupportAddressDto> addressDtoList = addressList.stream().map(address ->
				modelMapper.map(address, SupportAddressDto.class))
				.collect(Collectors.toList());

		return new ServiceMultiResult(addressDtoList.size(), addressDtoList);
	}

	@Override
	public ServiceMultiResult<SubwayStationDto> findAllStationBySubway(Integer subwayId) {
		if (subwayId == null) {
			return ServiceMultiResult.empty();
		}
		List<SubwayStation> stations = subwayStationRepository.findAllBySubwayId(subwayId);
		if (CollectionUtils.isEmpty(stations)) {
			return ServiceMultiResult.empty();
		}

		List<SubwayStationDto> subwayStationDtoList = stations.stream().map(station -> modelMapper.map(station, SubwayStationDto.class))
				.collect(Collectors.toList());
		return new ServiceMultiResult(subwayStationDtoList.size(), subwayStationDtoList);
	}

	@Override
	public ServiceMultiResult<SubwayDto> findAllSubwayByCity(String cityEnName) {
		if (StringUtils.isBlank(cityEnName)) {
			return ServiceMultiResult.empty();
		}

		List<Subway> subways = subwayRepository.findAllByCityEnName(cityEnName);
		if (CollectionUtils.isEmpty(subways)) {
			return ServiceMultiResult.empty();
		}

		List<SubwayDto> subwayDtoList = subways.stream().map(subway -> modelMapper.map(subway, SubwayDto.class))
				.collect(Collectors.toList());

		return new ServiceMultiResult<>(subwayDtoList.size(), subwayDtoList);
	}

	@Override
	public ServiceMultiResult<SupportAddressDto> findAllRegionsByCityName(String cityEnName) {
		if (StringUtils.isBlank(cityEnName)) {
			return ServiceMultiResult.empty();
		}
		List<SupportAddress> regions = supportAddressRepository.findAllByLevelAndBelongTo(SupportAddress.Level.REGION
				.getValue(), cityEnName);

		if (CollectionUtils.isEmpty(regions)) {
			return ServiceMultiResult.empty();
		}

		List<SupportAddressDto> result = regions.stream().map(region ->
				modelMapper.map(region, SupportAddressDto.class))
				.collect(Collectors.toList());

		return new ServiceMultiResult<>(result.size(), result);
	}

	@Override
	public Map<SupportAddress.Level, SupportAddressDto> findCitiesAndRegion(String cityEnName, String regionEnName) {
		Map<SupportAddress.Level, SupportAddressDto> result = new HashMap<>();

		SupportAddress city = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY
				.getValue());
		SupportAddress region = supportAddressRepository.findByEnNameAndBelongTo(regionEnName, city.getEnName());

		result.put(SupportAddress.Level.CITY, modelMapper.map(city, SupportAddressDto.class));
		result.put(SupportAddress.Level.REGION, modelMapper.map(region, SupportAddressDto.class));
		return result;
	}

	@Override
	public ServiceResult<SubwayDto> findSubway(Integer subwayId) {
		if (subwayId == null) {
			return ServiceResult.notFound();
		}
		Optional<Subway> optionalSubway = subwayRepository.findById(subwayId);
		if (optionalSubway == null || !optionalSubway.isPresent()) {
			return ServiceResult.notFound();
		}
		return ServiceResult.success(modelMapper.map(optionalSubway.get(), SubwayDto.class));
	}

	@Override
	public ServiceResult<SubwayStationDto> findSubwayStation(Integer stationId) {
		if (stationId == null) {
			return ServiceResult.notFound();
		}
		Optional<SubwayStation> optionalSubwayStation = subwayStationRepository.findById(stationId);
		if (optionalSubwayStation == null || !optionalSubwayStation.isPresent()) {
			return ServiceResult.notFound();
		}

		return ServiceResult.success(modelMapper.map(optionalSubwayStation.get(), SubwayStationDto.class));
	}

	@Override
	public Map<SupportAddress.Level, SupportAddressDto> findCityAndRegion(String cityEnName, String regionEnName) {
		Map<SupportAddress.Level, SupportAddressDto> result = Maps.newHashMap();

		SupportAddress city = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY.getValue());
		SupportAddress region = supportAddressRepository.findByEnNameAndBelongTo(regionEnName, city.getEnName());

		result.put(SupportAddress.Level.CITY, modelMapper.map(city, SupportAddressDto.class));
		result.put(SupportAddress.Level.REGION, modelMapper.map(region, SupportAddressDto.class));
		return result;
	}

	@Override
	public ServiceResult<SupportAddressDto> findCity(String cityEnName) {
		if (StringUtils.isBlank(cityEnName)) {
			return ServiceResult.notFound();
		}

		SupportAddress supportAddress = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY.getValue());
		if (supportAddress == null) {
			return ServiceResult.notFound();
		}

		SupportAddressDto addressDTO = modelMapper.map(supportAddress, SupportAddressDto.class);
		return ServiceResult.success(addressDTO);
	}
}
