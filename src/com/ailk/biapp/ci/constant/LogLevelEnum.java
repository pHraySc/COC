package com.ailk.biapp.ci.constant;

public enum LogLevelEnum
{
  Normal("01"),  Importance("02"),  Risk("03"),  Medium("04"),  Unknown("-1");
  
  private String value;
  
  public String getValue()
  {
    return this.value;
  }
  
  private LogLevelEnum(String value)
  {
    this.value = value;
  }
  
  public static LogLevelEnum fromValue(String value)
  {
    if (value.equals(Normal.getValue())) {
      return Normal;
    }
    if (value.equals(Importance.getValue())) {
      return Importance;
    }
    if (value.equals(Risk.getValue())) {
      return Risk;
    }
    if (value.equals(Medium.getValue())) {
      return Medium;
    }
    return Unknown;
  }
}
