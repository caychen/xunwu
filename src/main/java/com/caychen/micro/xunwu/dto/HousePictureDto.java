package com.caychen.micro.xunwu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Data
public class HousePictureDto {
    private Integer id;

    @JsonProperty(value = "house_id")
    private Integer houseId;

    private String path;

    @JsonProperty(value = "cdn_prefix")
    private String cdnPrefix;

    private int width;

    private int height;

}
