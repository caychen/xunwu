package com.caychen.micro.xunwu.web.controller.admin;

import com.caychen.micro.xunwu.enums.HouseOperationEnum;
import com.caychen.micro.xunwu.enums.HouseStatusEnum;
import com.caychen.micro.xunwu.dto.*;
import com.caychen.micro.xunwu.entity.SupportAddress;
import com.caychen.micro.xunwu.form.DatatableSearch;
import com.caychen.micro.xunwu.form.HouseForm;
import com.caychen.micro.xunwu.global.BaseResponse;
import com.caychen.micro.xunwu.global.ServiceMultiResult;
import com.caychen.micro.xunwu.global.ServiceResult;
import com.caychen.micro.xunwu.global.datatable.DataTableResponse;
import com.caychen.micro.xunwu.service.IHouseService;
import com.caychen.micro.xunwu.service.IQiNiuService;
import com.caychen.micro.xunwu.service.ISupportAddressService;
import com.caychen.micro.xunwu.vo.QiniuPutResult;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Controller
@RequestMapping("/admin")
@Api(tags = "管理员页面")
@Slf4j
public class AdminController {

	@Autowired
	private ISupportAddressService supportAddressService;

	@Autowired
	private IHouseService houseService;

	@Autowired
	private IQiNiuService qiNiuService;

	@Autowired
	private ISupportAddressService addressService;

	@GetMapping("/center")
	@ApiOperation("进入管理系统")
	public String center() {
		return "/admin/center";
	}

	@ApiOperation("进入管理系统欢迎页")
	@GetMapping("/welcome")
	public String welcome() {
		return "/admin/welcome";
	}

	@GetMapping("/login")
	@ApiOperation("管理员登录入口")
	public String login() {
		return "/admin/login";
	}

	@GetMapping("/add/house")
	public String addHousePage() {
		return "/admin/house-add";
	}

	@PostMapping(value = "/upload/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public BaseResponse uploadPhoto(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		if (file.isEmpty()) {
			return BaseResponse.ofStatus(BaseResponse.Status.PARAMETER_NOT_VALID);
		}

		try {
			InputStream inputStream = file.getInputStream();
			Response response = qiNiuService.uploadFile(inputStream);

			if (response.isOK()) {

				QiniuPutResult qiniuPutResult = new Gson().fromJson(response.bodyString(), QiniuPutResult.class);
				return BaseResponse.success(qiniuPutResult);
			} else {
				return BaseResponse.error(response.statusCode, response.getInfo());
			}

		} catch (QiniuException q) {
			Response response = q.response;
			try {
				return BaseResponse.error(response.statusCode, response.bodyString());
			} catch (QiniuException e) {
				log.error("获取七牛云返回异常", e);
				return BaseResponse.ofStatus(BaseResponse.Status.INTERNAL_SERVER_ERROR);
			}
		} catch (IOException e) {
			log.error("文件传输异常：", e);
			return BaseResponse.ofStatus(BaseResponse.Status.FILE_UPLOAD_ERROR);
		}

	}

	@PostMapping("/add/house")
	@ResponseBody
	public BaseResponse addHouse(@ModelAttribute("form-house-add") @Valid HouseForm houseForm,
	                             BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new BaseResponse(HttpStatus.BAD_REQUEST.value(), bindingResult.getAllErrors().get(0).getDefaultMessage(), null);
		}

		if (houseForm.getPhotos() == null || houseForm.getCover() == null) {
			return new BaseResponse(HttpStatus.BAD_REQUEST.value(), "必须上传图片", null);
		}

		Map<SupportAddress.Level, SupportAddressDto> addressDtoMap = supportAddressService.findCitiesAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());

		if (addressDtoMap.keySet().size() != 2) {
			return BaseResponse.ofStatus(BaseResponse.Status.PARAMETER_NOT_VALID);
		}

		ServiceResult<HouseDto> result = houseService.save(houseForm);
		if (result.isSuccess()) {
			return BaseResponse.success(result.getResult());
		} else {
			return BaseResponse.ofStatus(BaseResponse.Status.PARAMETER_NOT_VALID);
		}
	}

	/**
	 * 房源列表页
	 *
	 * @return
	 */
	@GetMapping("/house/list")
	public String houseListPage() {
		return "/admin/house-list";
	}

	@PostMapping("/houses")
	@ResponseBody
	public DataTableResponse houseList(@ModelAttribute DatatableSearch searchBody) {

		ServiceMultiResult<HouseDto> result = houseService.adminQuery(searchBody);
		DataTableResponse dataTableResponse = new DataTableResponse(BaseResponse.Status.SUCCESS);
		dataTableResponse.setData(result.getResult());
		dataTableResponse.setRecordsTotal(result.getTotal());
		dataTableResponse.setRecordsFiltered(result.getTotal());

		dataTableResponse.setDraw(searchBody.getDraw());

		return dataTableResponse;
	}

	/**
	 * 房源信息编辑页
	 *
	 * @return
	 */
	@GetMapping("/house/edit")
	public String houseEditPage(@RequestParam(value = "id") Integer id, Model model) {
		if (id == null || id < 1) {
			return "404";
		}

		//查询完整的房源信息
		ServiceResult<HouseDto> serviceResult = houseService.findCompleteOne(id);
		if (!serviceResult.isSuccess()) {
			return "404";
		}

		HouseDto houseDto = serviceResult.getResult();
		model.addAttribute("house", houseDto);

		Map<SupportAddress.Level, SupportAddressDto> citiesAndRegion =
				addressService.findCitiesAndRegion(houseDto.getCityEnName(), houseDto.getRegionEnName());
		model.addAttribute("city", citiesAndRegion.get(SupportAddress.Level.CITY));
		model.addAttribute("region", citiesAndRegion.get(SupportAddress.Level.REGION));

		HouseDetailDto houseDetailDto = houseDto.getHouseDetail();
		ServiceResult<SubwayStationDto> subwayStationServiceResult = addressService.findSubwayStation(houseDetailDto.getSubwayStationId());

		ServiceResult<SubwayDto> subwayServiceResult = addressService.findSubway(houseDetailDto.getSubwayLineId());
		if (subwayServiceResult.isSuccess()) {
			model.addAttribute("subway", subwayServiceResult.getResult());
		}

		if (subwayStationServiceResult.isSuccess()) {
			model.addAttribute("station", subwayStationServiceResult.getResult());
		}
		return "admin/house-edit";
	}

	/**
	 * 编辑接口
	 */
	@PostMapping("/house/edit")
	@ResponseBody
	public BaseResponse saveHouse(@Valid @ModelAttribute("form-house-edit") HouseForm houseForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		Map<SupportAddress.Level, SupportAddressDto> addressMap = addressService.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());

		if (addressMap.keySet().size() != 2) {
			return BaseResponse.ofStatus(BaseResponse.Status.PARAMETER_NOT_VALID);
		}

		ServiceResult result = houseService.update(houseForm);
		if (result.isSuccess()) {
			return BaseResponse.success();
		}

		BaseResponse response = BaseResponse.ofStatus(BaseResponse.Status.BAD_REQUEST);
		response.setMessage(result.getMessage());
		return response;
	}

	/**
	 * 移除图片接口
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/house/photo")
	@ResponseBody
	public BaseResponse removeHousePhoto(@RequestParam(value = "id") Integer id) {
		ServiceResult result = this.houseService.removePhoto(id);

		if (result.isSuccess()) {
			return BaseResponse.ofStatus(BaseResponse.Status.SUCCESS);
		} else {
			return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), result.getMessage());
		}
	}

	/**
	 * 修改封面接口
	 *
	 * @param coverId
	 * @param targetId
	 * @return
	 */
	@PostMapping("/house/cover")
	@ResponseBody
	public BaseResponse updateCover(@RequestParam(value = "cover_id") Integer coverId,
	                                @RequestParam(value = "target_id") Integer targetId) {
		ServiceResult result = this.houseService.updateCover(coverId, targetId);

		if (result.isSuccess()) {
			return BaseResponse.ofStatus(BaseResponse.Status.SUCCESS);
		} else {
			return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), result.getMessage());
		}
	}

	/**
	 * 增加标签接口
	 *
	 * @param houseId
	 * @param tag
	 * @return
	 */
	@PostMapping("/house/tag")
	@ResponseBody
	public BaseResponse addHouseTag(@RequestParam(value = "house_id") Integer houseId,
	                                @RequestParam(value = "tag") String tag) {
		if (houseId < 1 || StringUtils.isNoneBlank(tag)) {
			return BaseResponse.ofStatus(BaseResponse.Status.BAD_REQUEST);
		}

		ServiceResult result = this.houseService.addTag(houseId, tag);
		if (result.isSuccess()) {
			return BaseResponse.ofStatus(BaseResponse.Status.SUCCESS);
		} else {
			return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), result.getMessage());
		}
	}

	/**
	 * 移除标签接口
	 *
	 * @param houseId
	 * @param tag
	 * @return
	 */
	@DeleteMapping("/house/tag")
	@ResponseBody
	public BaseResponse removeHouseTag(@RequestParam(value = "house_id") Integer houseId,
	                                   @RequestParam(value = "tag") String tag) {
		if (houseId < 1 || StringUtils.isNoneBlank(tag)) {
			return BaseResponse.ofStatus(BaseResponse.Status.BAD_REQUEST);
		}

		ServiceResult result = this.houseService.removeTag(houseId, tag);
		if (result.isSuccess()) {
			return BaseResponse.ofStatus(BaseResponse.Status.SUCCESS);
		} else {
			return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), result.getMessage());
		}
	}

	/**
	 * 审核接口
	 *
	 * @param id
	 * @param operation
	 * @return
	 */
	@PutMapping("/house/operate/{id}/{operation}")
	@ResponseBody
	public BaseResponse operateHouse(@PathVariable(value = "id") Integer id,
	                                 @PathVariable(value = "operation") int operation) {
		if (id <= 0) {
			return BaseResponse.ofStatus(BaseResponse.Status.PARAMETER_NOT_VALID);
		}
		ServiceResult result;

		switch (operation) {
			case HouseOperationEnum.PASSED://审核通过，上架
				result = this.houseService.updateStatus(id, HouseStatusEnum.PASSED.getValue());
				break;
			case HouseOperationEnum.PULL_OUT://下架，需要审核
				result = this.houseService.updateStatus(id, HouseStatusEnum.NOT_AUDITED.getValue());
				break;
			case HouseOperationEnum.DELETE://删除
				result = this.houseService.updateStatus(id, HouseStatusEnum.DELETED.getValue());
				break;
			case HouseOperationEnum.RENT://出租
				result = this.houseService.updateStatus(id, HouseStatusEnum.RENTED.getValue());
				break;
			default:
				return BaseResponse.ofStatus(BaseResponse.Status.BAD_REQUEST);
		}

		if (result.isSuccess()) {
			return BaseResponse.success();
		}
		return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), result.getMessage());
	}

}
