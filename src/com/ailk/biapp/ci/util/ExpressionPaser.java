package com.ailk.biapp.ci.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class ExpressionPaser {
    private static Logger log = Logger.getLogger(ExpressionPaser.class);
    public static final String EXCEPT = "-";
    public static final String AND = "and";
    public static final String OR = "or";
    public static final String UNDERLINE = "_";
    public static final char LEFT_Q = '(';
    public static final char RIGHT_Q = ')';
    public static final String REG = "[0-9]*";

    public ExpressionPaser() {
    }

    public static String getNewString(String oldStr, int firstExceptPos) {
        char c = oldStr.charAt(firstExceptPos + 2);
        String front = oldStr.substring(0, firstExceptPos + 2);
        front = front.replace("-", "and");
        String middle = "";
        String back = oldStr.substring(firstExceptPos + 2);
        int newFirstExceptPos;
        if(c == 40) {
            int newStr = 0;
            newFirstExceptPos = 0;

            for(int newMiddleSb = 1; newMiddleSb < back.length(); ++newMiddleSb) {
                if(back.charAt(newMiddleSb) == 40) {
                    ++newStr;
                }

                if(newStr == 0 && back.charAt(newMiddleSb) == 41) {
                    newFirstExceptPos = newMiddleSb + 1;
                    break;
                }

                if(back.charAt(newMiddleSb) == 41) {
                    --newStr;
                }
            }

            if(newFirstExceptPos == 0) {
                String var13 = "没有找到对应的右括号，请检查条件是否缺少括号";
                log.error(var13);
                throw new RuntimeException(var13 + " : " + back);
            }

            middle = back.substring(0, newFirstExceptPos);
            if(newFirstExceptPos + 1 < back.length()) {
                back = back.substring(newFirstExceptPos + 1);
            } else {
                back = "";
            }

            StringBuffer var12 = new StringBuffer();
            if(middle.length() > 0) {
                String[] element = middle.split(" ");

                for(int j = 0; j < element.length; ++j) {
                    if(element[j].equals("and")) {
                        var12.append("or ");
                    } else if(element[j].equals("or")) {
                        var12.append("and ");
                    } else if(element[j].equals("-")) {
                        var12.append("or ");
                    } else if(isNumber(element[j])) {
                        if(j > 0 && element[j - 1].equals("-")) {
                            var12.append(element[j] + " ");
                        } else {
                            var12.append("_" + element[j] + " ");
                        }
                    } else {
                        var12.append(element[j] + " ");
                    }
                }
            }

            middle = var12.toString();
        } else {
            back = "_" + back;
        }

        String var11 = front + middle + back;
        newFirstExceptPos = var11.indexOf("-");
        if(newFirstExceptPos > 0) {
            var11 = getNewString(var11, newFirstExceptPos);
        }

        return var11;
    }

    public static boolean isNumber(String element) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(element);
        if(isNum.matches()) {
            flag = true;
        }

        return flag;
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("1 and 9 - 2 - 3 and ( 4 or 5 ) and 6 - ( 7 and 8 ) - ( 2 - 3 and ( 4 or 5 ) and ( 9 or ( 10 - 11) ) ) and 6 - 7 ");
        String oldStr = sb.substring(0, sb.lastIndexOf(" ")).toString();
        int firstPos = oldStr.indexOf("-");
        String resultStr = "";
        if(firstPos > 0) {
            resultStr = getNewString(oldStr, firstPos);
        } else {
            resultStr = oldStr;
        }

        System.out.println("1=============" + sb.toString());
        System.out.println("2=============" + resultStr);
    }
}
