package com.caychen.micro.xunwu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Data
public class SupportAddressDto {

	private Integer id;

	@JsonProperty(value = "belong_to")
	private String belongTo;

	@JsonProperty(value = "en_name")
	private String enName;

	@JsonProperty(value = "cn_name")
	private String cnName;

	private String level;
}
