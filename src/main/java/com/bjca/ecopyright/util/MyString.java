package com.bjca.ecopyright.util;

public class MyString
{

     public static void main(String args[])
     {

     }

     // 获取字符串
     public static String get(String value, String defaultValue)
     {
          if (value == null)
          {
               return defaultValue;
          }

          return value;
     }

     public static String get(String value)
     {
          return MyString.get(value, "");
     }

     // 处理非法字符
     public static String clearSqlChar(String oldStr)
     {
          String newStr = MyString.get(oldStr);

          newStr = newStr.replace("'", "");
          newStr = newStr.replace("\"", "");
          newStr = newStr.replace("/", "");
          newStr = newStr.replace("\\", "");
          newStr = newStr.replace("“", "");
          newStr = newStr.replace("‘", "");
          newStr = newStr.replace("\r", "");
          newStr = newStr.replace("\n", "");
          newStr = newStr.replace("\t", "");
          newStr = newStr.replace("_", "");
          newStr = newStr.replace("%", "");
          newStr = newStr.replace(" ", "");
          newStr = newStr.replace("or", "");
          newStr = newStr.replace("OR", "");
          newStr = newStr.replace("Or", "");
          newStr = newStr.replace("oR", "");
          newStr = newStr.replace(")", "");
          newStr = newStr.replace("(", "");
          newStr = newStr.replace("；", "");
          newStr = newStr.replace(";", "");
          newStr = newStr.replace("=", "");
          newStr = newStr.replace("?", "");
          newStr = newStr.replace("&", "");
          newStr = newStr.replace("@", "");

          return newStr;
     }

}
