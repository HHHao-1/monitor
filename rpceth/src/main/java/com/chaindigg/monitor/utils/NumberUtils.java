package com.chaindigg.monitor.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {
  public static BigDecimal weiToEth(BigInteger number) {
    BigDecimal eValue = new BigDecimal(String.valueOf(number));
    BigDecimal unit = BigDecimal.valueOf(Math.pow(10, 18));
    BigDecimal value = eValue.divide(unit);
    return value;
  }
}
