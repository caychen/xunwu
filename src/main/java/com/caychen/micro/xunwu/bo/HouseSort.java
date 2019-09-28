package com.caychen.micro.xunwu.bo;

import com.google.common.collect.Sets;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.Set;

/**
 * Author:       Caychen
 * Date:         2019/9/28
 * Desc:
 */
public class HouseSort {

	public static final String DEFAULT_SORT_KEY = "lastUpdateTime";

	public static final String DISTANCE_TO_SUBWAY_KEY = "distanceToSubway";

	public static final String CREATE_TIME_KEY = "createTime";

	public static final String PRICE_KEY = "price";

	public static final String AREA_KEY = "area";

	private static final Set<String> SORT_KEYS = Sets.newHashSet(
			DEFAULT_SORT_KEY,
			CREATE_TIME_KEY,
			PRICE_KEY,
			AREA_KEY,
			DISTANCE_TO_SUBWAY_KEY
	);

	public static Sort generateSort(String key, String directionKey){
		key = getSortKey(key);

		Optional<Sort.Direction> optionalDirection = Sort.Direction.fromOptionalString(directionKey);
		Sort.Direction direction = optionalDirection.orElse(Sort.Direction.DESC);

		return new Sort(direction, key);
	}

	public static String getSortKey(String key){
		if(!SORT_KEYS.contains(key)){
			key = DEFAULT_SORT_KEY;
		}

		return key;
	}
}
