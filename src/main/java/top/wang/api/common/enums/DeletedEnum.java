package top.wang.api.common.enums;

import lombok.Getter;

/**
 * 删除标识枚举
 *
 * @author mqxu
 */
@Getter
public enum DeletedEnum {

    /**
     * 未删除
     */
    NOT_DELETED(0, "未删除"),

    /**
     * 已删除
     */
    DELETED(1, "已删除");

    private final int value;
    private final String label;

    DeletedEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }
}
