package com.blisgo.util;

/**
 * 프론트단 템플릿을 선언하여 controller에서 페이지에서 type safe 하도록 구성
 *
 * @author okjae
 */
public class RouteUrlHelper {

    public static String combine(Object... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        // url 절대 경로 슬래시
        stringBuilder.append("/");
        for (Object str : strings) {

            stringBuilder.append(str);
            stringBuilder.append("/");
        }
        // REST 조건에 따른 마지막 슬래시 제거
        if (strings.length > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
