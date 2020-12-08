package com.chaindigg.monitor.common.utils;

public class StringUtils {
  public static String templateString(String origin, String... args) {
    StringBuffer result = new StringBuffer(origin);
    String rep = "{}";
    for (String arg : args) {
      int start = result.indexOf(rep);
      int end = start + rep.length();
      result.replace(start, end, arg);
    }
    return result.toString();
  }
  
}
