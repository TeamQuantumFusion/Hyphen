package dev.quantumfusion.hyphen.util;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CacheUtil {
	private static final boolean CACHE = true;

	public static <R, P> R cache(Map<? super P, R> cache, P param, Function<? super P, ? extends R> func) {
		if (CACHE) {
			if (cache.containsKey(param)) return cache.get(param);
			final R apply = func.apply(param);
			cache.put(param, apply);
			return apply;
		}
		return func.apply(param);
	}

	public static <R, P, D> R cache(Map<? super P, R> cache, P param, D data, BiFunction<? super P, ? super D, ? extends R> func) {
		if (CACHE) {
			if (cache.containsKey(param)) return cache.get(param);
			final R apply = func.apply(param, data);
			cache.put(param, apply);
			return apply;
		}
		return func.apply(param, data);
	}
}
