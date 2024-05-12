package com.xiaoxi.common.model.invokecontext.enums;

import com.xiaoxi.common.model.enums.UserRoleEnum;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ResponseCodeEnum {
    SUCCESS("成功", 200),
    NO_AUTH("没有权限", 403),
    NO_RESOURCE("剩余调用次数已为 0,请及时充值", 404),
    SYSTEM_ERROR("出现系统错误，请联系管理员", 500);

    private final String text;

    private final Integer value;

    ResponseCodeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     * @param value
     * @return
     */
    public static ResponseCodeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ResponseCodeEnum anEnum : ResponseCodeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
