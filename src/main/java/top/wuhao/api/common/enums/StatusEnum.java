package top.wuhao.api.common.enums;

import lombok.Getter;

/**
 * 状态枚举
 *
 * @author mqxu
 */
@Getter
public enum StatusEnum {

    /**
     * 禁用
     */
    DISABLE(0, "禁用"),

    /**
     * 正常
     */
    NORMAL(1, "正常");

    private final int value;
    private final String label;

    StatusEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }
}
