package org.rage.zeppelin.utils;

import java.util.Optional;
import java.util.function.Function;

import org.json.simple.JSONObject;

public final class FunctionUtils {
	public static final Function<Object, String> toString = (o) -> String.valueOf(o); 
	
	public static String getStringValueFromJsonObject(String propertyName, JSONObject jsonObject){
		final Optional<Object> optional = Optional.ofNullable(jsonObject.get(propertyName));
		return optional.isPresent() ?toString.apply(optional.get()) : null;
	}
}
