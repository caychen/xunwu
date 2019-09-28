package com.caychen.micro.xunwu.service;

import com.caychen.micro.xunwu.dto.HouseDto;
import com.caychen.micro.xunwu.form.DatatableSearch;
import com.caychen.micro.xunwu.form.HouseForm;
import com.caychen.micro.xunwu.form.RentSearch;
import com.caychen.micro.xunwu.global.ServiceMultiResult;
import com.caychen.micro.xunwu.global.ServiceResult;

/**
 * 房屋管理服务接口
 * Created by 瓦力.
 */
public interface IHouseService {

	ServiceResult<HouseDto> save(HouseForm houseForm);

	ServiceMultiResult<HouseDto> adminQuery(DatatableSearch searchBody);

	ServiceResult<HouseDto> findCompleteOne(Integer id);

	ServiceResult update(HouseForm houseForm);

	ServiceResult removePhoto(Integer id);

	ServiceResult updateCover(Integer coverId, Integer targetId);

	ServiceResult addTag(Integer houseId, String tag);

	ServiceResult removeTag(Integer houseId, String tag);

	ServiceResult updateStatus(Integer id, int status);

	ServiceMultiResult<HouseDto> query(RentSearch rentSearch);
}
