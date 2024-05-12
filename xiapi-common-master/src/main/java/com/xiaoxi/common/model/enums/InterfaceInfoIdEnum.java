package com.xiaoxi.common.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口id枚举类(已转为内部类使用，此类废弃)
 */
@Deprecated
public enum InterfaceInfoIdEnum {

    GetUserName("GetUserName", "1"),
    DoChatByGPT("DoChatByGPT", "2"),
    DoChatBySpark("DoChatBySpark", "2");

    private final String text;

    private final String value;

    InterfaceInfoIdEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static InterfaceInfoIdEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (InterfaceInfoIdEnum anEnum : InterfaceInfoIdEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
