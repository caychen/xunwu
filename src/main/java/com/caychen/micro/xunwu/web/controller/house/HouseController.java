package com.caychen.micro.xunwu.web.controller.house;

import com.caychen.micro.xunwu.common.RentValueBlock;
import com.caychen.micro.xunwu.dto.*;
import com.caychen.micro.xunwu.entity.SupportAddress;
import com.caychen.micro.xunwu.form.RentSearch;
import com.caychen.micro.xunwu.global.BaseResponse;
import com.caychen.micro.xunwu.global.ServiceMultiResult;
import com.caychen.micro.xunwu.global.ServiceResult;
import com.caychen.micro.xunwu.service.IHouseService;
import com.caychen.micro.xunwu.service.ISupportAddressService;
import com.caychen.micro.xunwu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Controller
@RequestMapping("/house")
public class HouseController {

	@Autowired
	private ISupportAddressService addressService;

	@Autowired
	private IHouseService houseService;

	@Autowired
	private IUserService userService;

	@GetMapping("/address/cities")
	@ResponseBody
	public BaseResponse getSupportCities(){
		ServiceMultiResult<SupportAddressDto> cities = addressService.findAllCities();
		if(cities.getResultSize() == 0){
			return BaseResponse.ofStatus(BaseResponse.Status.NOT_FOUND_DATA);
		}
		return BaseResponse.success(cities.getResult());
	}

	/**
	 * 获取对应城市支持区域列表
	 * @param cityEnName
	 * @return
	 */
	@GetMapping("/address/regions")
	@ResponseBody
	public BaseResponse getSupportRegions(@RequestParam(name = "city_name") String cityEnName) {
		ServiceMultiResult<SupportAddressDto> addressResult = addressService.findAllRegionsByCityName(cityEnName);
		if (addressResult.getResultSize() == 0) {
			return BaseResponse.ofStatus(BaseResponse.Status.NOT_FOUND_DATA);
		}
		return BaseResponse.success(addressResult.getResult());
	}

	/**
	 * 获取具体城市所支持的地铁线路
	 * @param cityEnName
	 * @return
	 */
	@GetMapping("/address/subway/line")
	@ResponseBody
	public BaseResponse getSupportSubwayLine(@RequestParam(name = "city_name") String cityEnName) {
		ServiceMultiResult<SubwayDto> subwayList = addressService.findAllSubwayByCity(cityEnName);
		if (subwayList.getResultSize() == 0) {
			return BaseResponse.ofStatus(BaseResponse.Status.NOT_FOUND_DATA);
		}

		return BaseResponse.success(subwayList.getResult());
	}

	/**
	 * 获取对应地铁线路所支持的地铁站点
	 * @param subwayId
	 * @return
	 */
	@GetMapping("/address/subway/station")
	@ResponseBody
	public BaseResponse getSupportSubwayStation(@RequestParam(name = "subway_id") Integer subwayId) {
		ServiceMultiResult<SubwayStationDto> stationDtoList = addressService.findAllStationBySubway(subwayId);
		if (stationDtoList.getResultSize() == 0) {
			return BaseResponse.ofStatus(BaseResponse.Status.NOT_FOUND_DATA);
		}

		return BaseResponse.success(stationDtoList.getResult());
	}

	@GetMapping("/rent")
	public String rentHousePage(@ModelAttribute RentSearch rentSearch,
	                            Model model, HttpSession session,
	                            RedirectAttributes redirectAttributes) {

		//进入首页默认该有个城市
		if (rentSearch.getCityEnName() == null) {
			String cityEnNameInSession = (String) session.getAttribute("cityEnName");
			if (cityEnNameInSession == null) {
				redirectAttributes.addAttribute("msg", "must_choose_city");
				return "redirect:/index";
			} else {
				rentSearch.setCityEnName(cityEnNameInSession);
			}
		} else {
			session.setAttribute("cityEnName", rentSearch.getCityEnName());
		}

		ServiceResult<SupportAddressDto> city = addressService.findCity(rentSearch.getCityEnName());
		if (!city.isSuccess()) {
			redirectAttributes.addAttribute("msg", "must_choose_city");
			return "redirect:/index";
		}
		model.addAttribute("currentCity", city.getResult());

		ServiceMultiResult<SupportAddressDto> addressResult = addressService.findAllRegionsByCityName(rentSearch.getCityEnName());
		if (addressResult.getResult() == null || addressResult.getTotal() < 1) {
			redirectAttributes.addAttribute("msg", "must_choose_city");
			return "redirect:/index";
		}

		ServiceMultiResult<HouseDto> serviceMultiResult = houseService.query(rentSearch);

		model.addAttribute("total", serviceMultiResult.getTotal());
		model.addAttribute("houses", serviceMultiResult.getResult());

		if (rentSearch.getRegionEnName() == null) {
			rentSearch.setRegionEnName("*");
		}

		model.addAttribute("searchBody", rentSearch);
		model.addAttribute("regions", addressResult.getResult());

		model.addAttribute("priceBlocks", RentValueBlock.PRICE_BLOCK);
		model.addAttribute("areaBlocks", RentValueBlock.AREA_BLOCK);

		model.addAttribute("currentPriceBlock", RentValueBlock.matchPrice(rentSearch.getPriceBlock()));
		model.addAttribute("currentAreaBlock", RentValueBlock.matchArea(rentSearch.getAreaBlock()));

		return "rent-list";
	}

	@GetMapping("/show/{id}")
	public String show(@PathVariable(value = "id") Integer houseId,
	                   Model model) {
		if (houseId <= 0) {
			return "404";
		}

		ServiceResult<HouseDto> serviceResult = houseService.findCompleteOne(houseId);
		if (!serviceResult.isSuccess()) {
			return "404";
		}

		HouseDto houseDTO = serviceResult.getResult();
		Map<SupportAddress.Level, SupportAddressDto>
				addressMap = addressService.findCityAndRegion(houseDTO.getCityEnName(), houseDTO.getRegionEnName());

		SupportAddressDto city = addressMap.get(SupportAddress.Level.CITY);
		SupportAddressDto region = addressMap.get(SupportAddress.Level.REGION);

		model.addAttribute("city", city);
		model.addAttribute("region", region);

		//经纪人
		ServiceResult<UserDto> userDTOServiceResult = userService.findById(houseDTO.getAdminId());
		model.addAttribute("agent", userDTOServiceResult.getResult());
		model.addAttribute("house", houseDTO);

		model.addAttribute("houseCountInDistrict", 0);
		//todo
//		ServiceResult<Long> aggResult = searchService.aggregateDistrictHouse(city.getEnName(), region.getEnName(), houseDTO.getDistrict());
//		model.addAttribute("houseCountInDistrict", aggResult.getResult());

		return "house-detail";
	}
}
