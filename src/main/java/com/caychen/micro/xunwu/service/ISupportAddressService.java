package com.caychen.micro.xunwu.service;

import com.caychen.micro.xunwu.dto.SubwayDto;
import com.caychen.micro.xunwu.dto.SubwayStationDto;
import com.caychen.micro.xunwu.dto.SupportAddressDto;
import com.caychen.micro.xunwu.entity.SupportAddress;
import com.caychen.micro.xunwu.global.ServiceMultiResult;
import com.caychen.micro.xunwu.global.ServiceResult;

import java.util.Map;

/**
 * Author:           Caychen
 * Date:             2019/9/8
 * Desc:
 */
public interface ISupportAddressService {

	ServiceMultiResult<SupportAddressDto> findAllCities();

	ServiceMultiResult<SubwayStationDto> findAllStationBySubway(Integer subwayId);

	ServiceMultiResult<SubwayDto> findAllSubwayByCity(String cityEnName);

	ServiceMultiResult<SupportAddressDto> findAllRegionsByCityName(String cityEnName);

	Map<SupportAddress.Level, SupportAddressDto> findCitiesAndRegion(String cityEnName, String regionEnName);

	ServiceResult<SubwayDto> findSubway(Integer subwayId) ;

	ServiceResult<SubwayStationDto> findSubwayStation(Integer subwayStationId);

	Map<SupportAddress.Level, SupportAddressDto> findCityAndRegion(String cityEnName, String regionEnName);

	ServiceResult<SupportAddressDto> findCity(String cityEnName);
}
