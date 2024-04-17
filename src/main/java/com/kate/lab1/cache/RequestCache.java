package com.kate.lab1.cache;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RequestCache {
  private static Map<String, Object> cache = new HashMap<>();

  private static final int MAX_SIZE = 100;

  public static void put(String key, Object value) {
    if (cache.size() == MAX_SIZE) {
      clear();
    }
    cache.put(key, value);
  }

  public static Object get(String key) {
    return cache.get(key);
  }

  public static void remove(String key) {
    cache.remove(key);
  }

  public static boolean containsKey(String key) {
    return cache.containsKey(key);
  }

  public static void clear() {
    cache.clear();
  }

  private RequestCache() {
  }

}
