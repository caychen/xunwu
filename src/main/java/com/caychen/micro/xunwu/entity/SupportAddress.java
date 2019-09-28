package com.caychen.micro.xunwu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Table
@Entity(name = "support_address")
@Data
public class SupportAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "belong_to")
	private String belongTo;//上一级行政单位名

	@Column(name = "en_name")
	private String enName;

	private String cnName;

	private String level;

	@Getter
	@AllArgsConstructor
	public enum Level{
		CITY("city"),
		REGION("region");

		private String value;

		public static Level of(String level){
			for (Level l : Level.values()) {
				if(StringUtils.equals(level,l.getValue() )){
					return l;
				}

			}
			throw new IllegalArgumentException();
		}
	}
}
