package org.rage.zeppelin.utils;

import java.util.function.Function;

public final class FunctionUtils {
	public static final Function<Object, String> toString = (o) -> String.valueOf(o); 
}
