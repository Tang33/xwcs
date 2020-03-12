package fast.main.util;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

/**
 * Created by IntelliJ IDEA.
 * User: phoenix
 * Date: 2004-2-23
 * Time: 17:28:07
 * 字符串型变量操作函数类
 */
public class StringUtil {
    /**
     * 字符串如为空值，则转换为空字符串
     * @param string    字符串
     * @return  非空字符串
     */
    public static String parseNull(String string) {
        if (null != string && !"".equals(string)) {
        } else {
            string = new String("");
        }
        return string;
    }

    /**
     * 字符串替换（JDK 1.4 以后版本已有替代函数）
     * @param sourceString      源字符串
     * @param toReplaceString   被替换的字符串
     * @param replaceString     替换成的字符串
     * @return 结果字符串
     */
    public static String stringReplace(String sourceString,
                                       String toReplaceString,
                                       String replaceString) {
        String returnString = sourceString;
        try {
//            if (StartupClazz.JDK_VERSION.substring(0,3).equals("1.4")) {
//                Class[] paramType = new Class[] {String.class, String.class};
//                Method m = sourceString.getClass().getMethod("replaceAll", paramType);
//                String[] args = new String[2];
//                args[0] = toReplaceString;
//                args[1] = replaceString;
//                returnString = (String) m.invoke(sourceString, args);
//            } else {
            int stringLength = 0;
            if (toReplaceString != null) {
                stringLength = toReplaceString.length();
            }
            if (returnString != null && returnString.length() > stringLength) {
                int max = 0;
                String S4 = "";
                for (int i = 0; i < sourceString.length(); i++) {
                    max = i + toReplaceString.length() > sourceString.length() ?
                          sourceString.length() : i + stringLength;
                    String S3 = sourceString.substring(i, max);
                    if (!S3.equals(toReplaceString)) {
                        S4 += S3.substring(0, 1);
                    } else {
                        S4 += replaceString;
                        i += stringLength - 1;
                    }
                }
                returnString = S4;
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }

    /**
     * 查找某一字符串中特定子串的出现次数
     * @param sourceString  源字符串
     * @param sign          标记字符串
     * @return  出现次数
     */
    public static int getSplitCount(String sourceString, String sign) {
        if (sourceString == null) {
            return 0;
        }
        StringTokenizer all = new StringTokenizer(sourceString, sign, true);
        StringTokenizer content = new StringTokenizer(sourceString, sign, false);
        int countAll = all.countTokens();
        int countContent = content.countTokens();
        //System.out.println("===" + countAll + "===" + countContent + "===");
        return (countAll - countContent) / sign.length() + 1;
    }

    /**
     * 按特定子串为标记，将子串截成数组（JDK 1.4 以后版本已有替代函数）
     * @param sourceString  源字符串
     * @param sign          标记字符串
     * @return  子串数组
     */
    public static String[] stringSplit(String sourceString, String sign) {
        String[] arr = null;
//        try {
//            if (StartupClazz.JDK_VERSION.substring(0,3).equals("1.4")) {
//                if (null == sourceString)
//                    sourceString = "";
//                Class[] paramType = new Class[] {String.class};
//                Method m = sourceString.getClass().getMethod("split", paramType);
//                Object[] args = new Object[1];
//                args[0] = sign;
//                arr = (String[]) m.invoke(sourceString, args);
//            } else {
        int count = getSplitCount(sourceString, sign);
        if (count == 0) {
            return null;
        }
        int j = 0;

        arr = new String[count];
        for (int i = 0; i < count; i++) {
            if (sourceString.indexOf(sign) != -1) {
                j = sourceString.indexOf(sign);
                arr[i] = sourceString.substring(0, j);
                sourceString = sourceString.substring(j + sign.length());
            } else {
                arr[i] = sourceString;
            }
        }
//            }
//        } catch (NoSuchMethodException e) {
//            LoggerUtil.error(":::::Web Application [Doel]:::::", e);
//        } catch (IllegalAccessException e) {
//            LoggerUtil.error(":::::Web Application [Doel]:::::", e);
//        } catch (InvocationTargetException e) {
//            LoggerUtil.error(":::::Web Application [Doel]:::::", e);
//        }
        return arr;
    }

    /**
     * 格式化字符串，向前面自动补零
     * @param source    原字符串
     * @param length    格式化后长度
     * @return  格式化后字符串
     */
    public static String formatStringRight(String source, int length) {
        String strZero = "";
        int numZero = length - source.trim().length();
        for (int i = 1; i <= numZero; i++) {
            strZero = strZero + "0";
        }
        return strZero + source;
    }

    /**
     * 格式化字符串，向前面自动补零
     * @param source    原字符串
     * @param length    格式化后长度
     * @return  格式化后字符串
     */
    public static String formatStringLeft(String source, int length) {
        String strZero = "";
        int numZero = length - source.trim().length();
        for (int i = 1; i <= numZero; i++) {
            strZero = "0" + strZero;
        }
        return strZero + source;
    }

    /**
     * 去除字符串左边的“0”
     * @param source    原字符串
     * @return 字符串结果
     */
    public static String trimZeroLeft(String source) {
        try {
            return String.valueOf(Long.parseLong(source, 10));
        } catch (Exception ex) {
            return source;
        }
    }

    /**
     * 去除字符串右边的“0”
     * @param source    原字符串
     * @return 字符串结果
     */
    public static String trimZeroRight(String source) {
        try {
            StringBuffer stringBuffer = new StringBuffer(source);
            String strTemp = stringBuffer.reverse().toString();
            strTemp = String.valueOf(Long.parseLong(strTemp));
            stringBuffer = new StringBuffer(strTemp);
            return stringBuffer.reverse().toString();
        } catch (Exception ex) {
            return source;
        }
    }
	/**
	 * 格式化数据，当数据前面第一位为"."的时候，在此数据前加0
	 * @param str
	 * @return
	 */
    public static String foramtStr(String str){
    	if(str!=null && !"".equals(str) && !"null".equals(str)){
    		if(".".equals(str.substring(0,1))){
    			str = "0"+str;
    		}
    	}
    	return str;
    }
    /**
     * 去除数组单元中的空格
     * @param source 原字符串数组
     * @return 结果字符串数组
     */
    public static String[] trimArray(String[] source) {
        String[] result = source;
        for (int i = 0; i < source.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }
//    /**
//     * 将给定的字符串格式化为定长字符串, 原始字符串长度超过给定长度的,按照给定长度从左到右截取 如果原始字符串小于给定长度,
//     * 则按照给定字符在左端补足空位
//     *
//     * @param src
//     *            原始字符串
//
//     * @param s2
//     *            补充用字符,
//     * @param length
//     *            格式化后长度
//     * @return 格式化后字符串
//
//     */
//    public static String formatString(String src, char s2, int length) {
//      String retValue = src;
//      if (src == null || length <= 0) {
//        return null;
//      }
//
//      if (src.length() > length) {
//        retValue = src.substring(0, length);
//      }
//
//      for (int i = 0; i < length - src.length(); i++) {
//        retValue = s2 + retValue;
//      }
//
//      return retValue;
//    }

    /**
     * 字符串的字符集类型转换

     * @param src --需要转换的字符串

     * @param fromCharSet --字符串当前的字符集类型，如"iso-8859-1","GBK"等

     * @param toCharSet --目标字符集类型，如"iso-8859-1","GBK"等

     * @return --转换后的字符串,失败返回null
     */
    public static String charSetConvert(String src, String fromCharSet,
                                        String toCharSet) {
      if (src == null) {
        return src;
      }
      try {
        return new String(src.getBytes(fromCharSet), toCharSet);
      }
      catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }

    /**
     * 将iso8859的字符集转换成UTF-8字符集

     * @param src --iso8859字符串

     * @return --转化后的字符串,失败返回null
     */
    public static String isoToUTF8(String src) {
      return charSetConvert(src, "iso-8859-1", "UTF-8");
    }

    /**
     * 将iso8859的字符集转换成GBK字符集

     * @param src --iso8859字符串

     * @return --转化后的字符串,失败返回null
     */
    public static String isoToGBK(String src) {
      return charSetConvert(src, "iso-8859-1", "GBK");
    }

    /**
     * 将GBK的字符集转换成iso8859字符集

     * @param src --GBK字符串

     * @return --转化后的字符串,失败返回null
     */
    public static String gbkToISO(String src) {
      return charSetConvert(src, "GBK", "iso-8859-1");
    }

    /**
     * 字符替换函数 <br>
     * 比如 String a = "ccddaa"; replace(a,"dd","xx") 将返回 ccxxaa
     *
     * @param str
     *            需要替换的原始字符串

     * @param pattern 
     *            需要被替换掉的字符串

     * @param replace
     *            希望被替换成的字符串
     * @return 返回替换后的字符串

     */
    public static String replace(String str, String pattern, String replace) {
      int s = 0;
      int e = 0;
      StringBuffer result = new StringBuffer();
      while ( (e = str.indexOf(pattern, s)) >= 0) {
        result.append(str.substring(s, e));
        result.append(replace);
        s = e + pattern.length();
      }
      result.append(str.substring(s));
      return result.toString();
    }

    /**
     * 重复一个字符串 n 次，比如 abcabcabc
     *
     * @param str
     *            需要重复的字符串

     * @param repeat
     *            重复的次数

     * @return 重复后生成的字符串

     */
    public static String repeat(String str, int repeat) {
      StringBuffer buffer = new StringBuffer(repeat * str.length());
      for (int i = 0; i < repeat; i++) {
        buffer.append(str);
      }
      return buffer.toString();
    }

    /**
     * 获得一个字符串的最左边的n个字符，如果长度len的长度大于字符串的总长度，返回字符串本身

     *
     * @param str
     *            原始的字符串
     * @param len
     *            左边的长度

     * @return 最左边的字符

     */
    public static String left(String str, int len) {
      if (len < 0) {
        throw new IllegalArgumentException("Requested String length " + len
                                           + " is less than zero");
      }
      if ( (str == null) || (str.length() <= len)) {
        return str;
      }
      else {
        return str.substring(0, len);
      }
    }

    /**
     * 获得一个字符串的最右边的n个字符，如果长度len的长度大于字符串的总长度，返回字符串本身

     *
     * @param str
     *            原始的字符串
     * @param len
     *            右边的长度

     * @return 最右边的字符

     */
    public static String right(String str, int len) {
      if (len < 0) {
        throw new IllegalArgumentException("Requested String length " + len
                                           + " is less than zero");
      }
      if ( (str == null) || (str.length() <= len)) {
        return str;
      }
      else {
        return str.substring(str.length() - len);
      }
    }

    /**
     * 将给定的字符串格式化为定长字符串, 原始字符串长度超过给定长度的,按照给定长度从左到右截取 如果原始字符串小于给定长度,
     * 则按照给定字符在左端补足空位
     *
     * @param src
     *            原始字符串

     * @param s2
     *            补充用字符,
     * @param length
     *            格式化后长度
     * @return 格式化后字符串

     */
    public static String formatString(String src, char s2, int length) {
      String retValue = src;
      if (src == null || length <= 0) {
        return null;
      }

      if (src.length() > length) {
        retValue = src.substring(0, length);
      }

      for (int i = 0; i < length - src.length(); i++) {
        retValue = s2 + retValue;
      }

      return retValue;
    }

    /**
     * 将一个浮点数转换为人民币的显示格式：￥##,###.##
     *
     * @param value
     *            浮点数

     * @return 人民币格式显示的数字
     */
    public static String toRMB(double value) {
      NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
      return nf.format(value);
    }

    /**
     * 默认保留小数点后两位，将一个浮点数转换为定长小数位的小数 ######.##
     *
     * @param value
     *            浮点数

     * @return 定长小数位的小数
     */
    public static String toCurrencyWithoutComma(double value) {
      String retValue = toCurrency(value);
      retValue = retValue.replaceAll(",", "");
      return retValue;
    }

    /**
     * 默认保留小数点后两位，将一个浮点数转换为货币的显示格式：##,###.##
     *
     * @param value
     *            浮点数

     * @return 货币格式显示的数字

     */
    public static String toCurrency(double value) {
      return toCurrency(value, 2);
    }

    /**
     * 根据指定的小数位数，将一个浮点数转换为货币的显示格式
     *
     * @param value
     *            浮点数

     * @param decimalDigits
     *            小数点后保留小数位数
     * @return 货币格式显示的数字 <br>
     *         <br>
     *         例： toCurrency(123456.789,5) 将返回 "123,456.78900"
     */
    public static String toCurrency(double value, int decimalDigits) {
      String format = "#,##0." + repeat("0", decimalDigits);
      NumberFormat nf = new DecimalFormat(format);
      return nf.format(value);

    }

    /**
     * 将一个字符串格式化为给定的长度，过长的话按照给定的长度从字符串左边截取，反之以给定的 字符在字符串左边补足空余位 <br>
     * 比如： <br>
     * prefixStr("abc",'0',5) 将返回 00aaa <br>
     * prefixStr("abc",'0',2) 将返回 ab
     *
     * @param source
     *            原始字符串

     * @param profix
     *            补足空余位时使用的字符串
     * @param length
     *            格式化后字符串长度

     * @return 返回格式化后的字符串,异常返回null
     */
    public static String prefixStr(String source, char profix, int length) {
      String strRet = source;
      if (source == null) {
        return strRet;
      }
      if (source.length() >= length) {
        strRet = source.substring(0, length);
      }

      if (source.length() < length) {
        for (int i = 0; i < length - source.length(); i++) {
          strRet = "" + profix + strRet;
        }
      }

      return strRet;
    }
    /**
     * 判断字符串是否为空

     * @param str
     * @return
     */
    public static boolean isBlank( String str){
        if (str == null || str.trim().length() == 0 || "null".equals(str)){
          return true;
        }
        return false;
    }
    /**
     * 格式化字符串,将字符串trim()后返回. 如果字符串为null,则返回长度为零的字符串("")
     *
     * @param value
     *            被格式化字符串

     * @return 格式化后的字符串
     */
    public static String stringTrim(String value) {
      if (value == null) {
        return "";
      }
      return value.trim();
    }

    /**
     * 格式化字符串,将字符串trim()后返回. 如果字符串为null,则返回长度为零的字符串("")
     *
     * @param value
     *            被格式化字符串

     * @return 格式化后的字符串
     */
    public static String strObjTrim(Object value) {
      if (value == null) {
        return "";
      }
      return value.toString().trim();
    }

    /**
     * 将一个字符串格式化为给定的长度，过长的话按照给定的长度从字符串左边截取，反之以给定的 字符在字符串右边补足空余位 <br>
     * 比如： <br>
     * suffixStr("abc",'0',5) 将返回 aaa00 <br>
     * suffixStr("abc",'0',2) 将返回 ab
     *
     * @param source
     *            原始字符串

     * @param profix
     *            补足空余位时使用的字符串
     * @param length
     *            格式化后字符串长度

     * @return 返回格式化后的字符串,异常返回null
     */
    public static String suffixStr(String source, char suffix, int length) {
      String strRet = source;
      if (source == null) {
        return strRet;
      }
      if (source.length() >= length) {
        strRet = source.substring(0, length);
      }

      if (source.length() < length) {
        for (int i = 0; i < length - source.length(); i++) {
          strRet += suffix;
        }
      }
      return strRet;
    }

    /**
     * 根据分割符sp，将str分割成多个字符串，并将它们放入一个ArrayList并返回，其规则是最后的 字符串最后add到ArrayList中

     *
     * @param str
     *            被分割的字符串

     * @param sp
     *            分割符字符串
     * @return 封装好的ArrayList
     * @author 高虹 ： 友情提供
     */
    public static ArrayList convertStrToArrayList(String str, String sp) {
      ArrayList al = new ArrayList();
      if (str == null) {
        return al;
      }
      String strArr[] = str.split(sp);
      for (int i = 0; i < strArr.length; i++) {
        al.add(strArr[i]);
      }

      return al;
    }

    /**
     * 将数字字符串转换为人民币大写
     *
     * @param value
     *            金额人民币数字字符串
     * @return 转换后的人民币大写字符串
     * @author 王德春 : 友情提供
     */
    public static String changeToBig(String value) {

      if (null == value || "".equals(value.trim())) {
        return "零";
      }
      // add by Johnson Zhang
      int len = value.length();

      String strCheck, strArr, strFen, strDW, strNum, strBig, strNow;
      double d = 0;
      try {
        d = Double.parseDouble(value);
      }
      catch (Exception e) {
        return "数据" + value + "非法！";
      }

      strCheck = value + ".";
      int dot = strCheck.indexOf(".");
      if (dot > 12) {
        return "数据" + value + "过大，无法处理！";
      }

      try {
        int i = 0;
        strBig = "";
        strDW = "";
        strNum = "";

        // long intFen = (long)(d*100); //原来的处理方法

        /**
         * 增加了对double转换为long时的精度， 例如:(long)(208123.42 * 100) = 20812341
         * 解决了该问题 add 庞学亮

         */
        BigDecimal big = new BigDecimal(d);
        big = big.multiply(new BigDecimal(100)).setScale(2, 4);
        long intFen = big.longValue();

        strFen = String.valueOf(intFen);
        int lenIntFen = strFen.length();
        while (lenIntFen != 0) {
          i++;
          switch (i) {
            case 1:
              strDW = "分";
              break;
            case 2:
              strDW = "角";
              break;
            case 3:
              strDW = "元";
              break;
            case 4:
              strDW = "拾";
              break;
            case 5:
              strDW = "佰";
              break;
            case 6:
              strDW = "仟";
              break;
            case 7:
              strDW = "万";
              break;
            case 8:
              strDW = "拾";
              break;
            case 9:
              strDW = "佰";
              break;
            case 10:
              strDW = "仟";
              break;
            case 11:
              strDW = "亿";
              break;
            case 12:
              strDW = "拾";
              break;
            case 13:
              strDW = "佰";
              break;
            case 14:
              strDW = "仟";
              break;
          }
          switch (strFen.charAt(lenIntFen - 1)) { // 选择数字

            case '1':
              strNum = "壹";
              break;
            case '2':
              strNum = "贰";
              break;
            case '3':
              strNum = "叁";
              break;
            case '4':
              strNum = "肆";
              break;
            case '5':
              strNum = "伍";
              break;
            case '6':
              strNum = "陆";
              break;
            case '7':
              strNum = "柒";
              break;
            case '8':
              strNum = "捌";
              break;
            case '9':
              strNum = "玖";
              break;
            case '0':
              strNum = "零";
              break;
          }
          // 处理特殊情况
          strNow = strBig;
          // 分为零时的情况

          if ( (i == 1) && (strFen.charAt(lenIntFen - 1) == '0')) {
            strBig = "整";
            // 角为零时的情况

          }
          else if ( (i == 2) && (strFen.charAt(lenIntFen - 1) == '0')) { // 角分同时为零时的情况
            if (!strBig.equals("整")) {
              strBig = "零" + strBig;
            }
          }
          // 元为零的情况
          else if ( (i == 3) && (strFen.charAt(lenIntFen - 1) == '0')) {
            strBig = "元" + strBig;
            // 拾－仟中一位为零且其前一位（元以上）不为零的情况时补零

          }
          else if ( (i < 7) && (i > 3)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && (! ("" + strNow.charAt(0)).equals("零"))
                   && (! ("" + strNow.charAt(0)).equals("元"))) {
            strBig = "零" + strBig;
            // 拾－仟中一位为零且其前一位（元以上）也为零的情况时跨过

          }
          else if ( (i < 7) && (i > 3)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && ( ("" + strNow.charAt(0)).equals("零"))) {
          }
          // 拾－仟中一位为零且其前一位是元且为零的情况时跨过
          else if ( (i < 7) && (i > 3)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && ( ("" + strNow.charAt(0)).equals("元"))) {
          }
          // 当万为零时必须补上万字

          // modified by Johnson Zhang
          else if ( (i == 7) && (strFen.charAt(lenIntFen - 1) == '0')) {
            strBig = "万" + strBig;
            // 拾万－仟万中一位为零且其前一位（万以上）不为零的情况时补零

          }
          else if ( (i < 11) && (i > 7)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && (! ("" + strNow.charAt(0)).equals("零"))
                   && (! ("" + strNow.charAt(0)).equals("万"))) {
            strBig = "零" + strBig;
            // 拾万－仟万中一位为零且其前一位（万以上）也为零的情况时跨过

          }
          else if ( (i < 11) && (i > 7)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && ( ("" + strNow.charAt(0)).equals("万"))) {
          }
          // 拾万－仟万中一位为零且其前一位为万位且为零的情况时跨过

          else if ( (i < 11) && (i > 7)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && ( ("" + strNow.charAt(0)).equals("零"))) {
          }
          // 万位为零且存在仟位和十万以上时，在万仟间补零
          else if ( (i < 11) && (i > 8)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && ( ("" + strNow.charAt(0)).equals("万"))
                   && ( ("" + strNow.charAt(2)).equals("仟"))) {
            strBig = strNum + strDW + "万零"
                + strBig.substring(1, strBig.length());
            // 单独处理亿位
          }
          else if (i == 11) {
            // 亿位为零且万全为零存在仟位时，去掉万补为零

            if ( (strFen.charAt(lenIntFen - 1) == '0')
                && ( ("" + strNow.charAt(0)).equals("万"))
                && ( ("" + strNow.charAt(2)).equals("仟"))) {
              strBig = "亿" + "零"
                  + strBig.substring(1, strBig.length());
              // 亿位为零且万全为零不存在仟位时，去掉万

            }
            else if ( (strFen.charAt(lenIntFen - 1) == '0')
                     && ( ("" + strNow.charAt(0)).equals("万"))
                     && ( ("" + strNow.charAt(2)).equals("仟"))) {
              strBig = "亿" + strBig.substring(1, strBig.length());
              // 亿位不为零且万全为零存在仟位时，去掉万补为零
            }
            else if ( ( ("" + strNow.charAt(0)).equals("万"))
                     && ( ("" + strNow.charAt(2)).equals("仟"))) {
              strBig = strNum + strDW + "零"
                  + strBig.substring(1, strBig.length());
              // 亿位不为零且万全为零不存在仟位时，去掉万
            }
            else if ( ( ("" + strNow.charAt(0)).equals("万"))
                     && ( ("" + strNow.charAt(2)).equals("仟"))) {
              strBig = strNum + strDW
                  + strBig.substring(1, strBig.length());
              // 其他正常情况
            }
            else {
              if ( ("" + strBig.charAt(0)).equals("万")) {
                strBig = strNum + strDW
                    + strBig.substring(1, strBig.length())
                    + " ";
              }
              else {
                strBig = strNum + strDW + strBig;
              }
            }

          } // 拾亿－仟亿中一位为零且其前一位（亿以上）不为零的情况时补零

          else if ( (i < 15) && (i > 11)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && ( ("" + strNow.charAt(0)).equals("零"))
                   && (! ("" + strNow.charAt(0)).equals("亿"))) {
            strBig = "零" + strBig;
            // 拾亿－仟亿中一位为零且其前一位（亿以上）也为零的情况时跨过

          }
          else if ( (i < 15) && (i > 11)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && ( ("" + strNow.charAt(0)).equals("亿"))) {
          }
          // 拾亿－仟亿中一位为零且其前一位为亿位且为零的情况时跨过

          else if ( (i < 15) && (i > 11)
                   && (strFen.charAt(lenIntFen - 1) == '0')
                   && ( ("" + strNow.charAt(0)).equals("零"))) {
          }
          // 亿位为零且不存在仟万位和十亿以上时去掉上次写入的零

          else if ( (i < 15) && (i > 11)
                   && (strFen.charAt(lenIntFen - 1) != '0')
                   && ( ("" + strNow.charAt(0)).equals("零"))
                   && ( ("" + strNow.charAt(1)).equals("亿"))
                   && (! ("" + strNow.charAt(3)).equals("仟"))) {
            strBig = strNum + strDW
                + strBig.substring(1, strBig.length());
            // 亿位为零且存在仟万位和十亿以上时，在亿仟万间补零
          }
          else if ( (i < 15) && (i > 11)
                   && (strFen.charAt(lenIntFen - 1) != '0')
                   && ( ("" + strNow.charAt(0)).equals("零"))
                   && ( ("" + strNow.charAt(1)).equals("亿"))
                   && ( ("" + strNow.charAt(3)).equals("仟"))) {
            strBig = strNum + strDW + "亿零"
                + strBig.substring(2, strBig.length());
          }
          else {
            strBig = strNum + strDW + strBig;
          }
          strFen = strFen.substring(0, lenIntFen - 1);
          lenIntFen--;
        }
//        strBig = isoToUTF8(strBig);
        return strBig;
      }
      catch (Exception e) {
        return "";
      }
    }

    // add by kfr for date
    public static String changeToBigdate(String value) {

      if (null == value || "".equals(value.trim())) {
        return " ";
      }

      String strDW, strNum, strNum1;

      strDW = "";
      strNum = "";
      strNum1 = "";

      for (int i = 0; i < value.length(); i++) {
        if (value.length() == 2) {
          if (i == 0) {
            switch (value.substring(0, 1).charAt(i)) {
              case '1':
                strNum = "十";
                break;
              case '2':
                strNum = "二十";
                break;
              case '3':
                strNum = "三十";
                break;
              case '0':
                strNum = " ";
                break;

            }
            switch (value.charAt(1)) { // 选择数字
              case '1':
                strNum1 = "一";
                break;
              case '2':
                strNum1 = "二";
                break;
              case '3':
                strNum1 = "三";
                break;
              case '4':
                strNum1 = "四";
                break;
              case '5':
                strNum1 = "五";
                break;
              case '6':
                strNum1 = "六";
                break;
              case '7':
                strNum1 = "七";
                break;
              case '8':
                strNum1 = "八";
                break;
              case '9':
                strNum1 = "九";
                break;
              case '0':
                strNum1 = " ";
                break;
            }

            strDW = strNum + strNum1;
          }
        }

        else {
          if (i == 0 && value.startsWith("0")) {
            strNum = "";
          }
          else {
            switch (value.charAt(i)) { // 选择数字
              case '1':
                strNum = "一";
                break;
              case '2':
                strNum = "二";
                break;
              case '3':
                strNum = "三";
                break;
              case '4':
                strNum = "四";
                break;
              case '5':
                strNum = "五";
                break;
              case '6':
                strNum = "六";
                break;
              case '7':
                strNum = "七";
                break;
              case '8':
                strNum = "八";
                break;
              case '9':
                strNum = "九";
                break;
              case '0':
                strNum = "零";
                break;
            }
          }
          strDW = strDW + strNum;
        }
      }

      return strDW;
    }
    /**
     * 先判断传入数据是否为0,如果为0表示没有取得值,给变量赋初始值:2008-10-10
     * 然后将其转化成可以插入数据科date列的值.即:to_date('xxx','yyyy-mm-dd')
     * @param date
     * @return
     */
	public static String to_date(String date){
		if("0".equals(date)){
			date = "2008-10-10";
		}else{
			date = date.substring(0,10);
		}
		date = "to_date('"+date+"','yyyy-mm-dd')";	
		return date;
	}
	/**
	 * 判断传入的值是否为空,如果为空则给其赋为0
	 * @param str
	 * @return
	 */
	public static String defaultZero(String str){
		if(StringUtil.isBlank(str)){
			str = "0";
		}
		return str;
	}
	
	/**
	 * 取excel中的信息,
	 * 根据Cell的类型，提取对应Value 参数说明 HSSFCell cell 参数 type ，
	 * N 纳税人识别号 S 字符串 "" V 数值 D为日期,并拼成to_date('','yyyy-mm-dd')样式
		
	
	 * null D 日期 null
	 */
	public static String getCellValue(HSSFCell cell, String type) {
		String returnValue = "";
		if (cell == null) {
			if ("S".equals(type) || "N".equals(type)) {
				 returnValue = "";
			} else if ("V".equals(type)) {
				 returnValue = " null ";

			} else if ("D".equals(type)) {
				 returnValue = " null ";
			}
		} else {
			if ("D".equals(type)) {
				String d_date = DateUtil.parseUtilToString(cell.getDateCellValue(),"yyyy-MM-dd");
				System.out.println(d_date);
				if(StringUtil.isBlank(d_date)){
					d_date = "2008-10-10";
				}
				 returnValue = "to_date('"+ d_date + "','yyyy-mm-dd')";
			}
			
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				returnValue = cell.getStringCellValue().trim();
				if ("N".equals(type)) {
					if (returnValue.indexOf(".") > 0) {
						double sbh = cell.getNumericCellValue();
						BigDecimal big = new BigDecimal(sbh);
						 returnValue = String.valueOf(big.setScale(0));
					}
				}
			} else if (HSSFDateUtil.isCellDateFormatted(cell)) {
				String get_date = DateUtil.parseUtilToString(cell.getDateCellValue(),"yyyy-MM-dd");
				if(StringUtil.isBlank(get_date)){
					get_date = "2008-10-10";
				}
				 returnValue = "to_date('"
						+ get_date + "','yyyy-mm-dd')";
				
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				returnValue = String.valueOf(cell.getNumericCellValue());
				if ("N".equals(type)) {
					if (returnValue.indexOf(".") > 0) {
						double sbh = cell.getNumericCellValue();
						BigDecimal big = new BigDecimal(sbh);
						 returnValue = String.valueOf(big.setScale(0));
					}
				}

			}
		}
		if(StringUtil.isBlank(returnValue) && "V".equals(type)){
			returnValue = "0";
		}
		return returnValue;
	}
}
