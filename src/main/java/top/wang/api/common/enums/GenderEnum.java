package top.wang.api.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author mqxu
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {
    /**
     * 未知
     */
    UNKNOWN(0, "未知"),
    /**
     * 男
     */
    MALE(1, "男"),
    /**
     * 女
     */
    FEMALE(2, "女");

    @EnumValue  // MyBatis-Plus 注解,标记存储到数据库的值
    private final Integer value;
    
    @JsonValue  // Jackson 注解,序列化时返回的值
    private final String desc;

    /**
     * 根据值获取枚举
     */
    public static GenderEnum getByValue(Integer value) {
        if (value == null) {
            return UNKNOWN;
        }
        for (GenderEnum gender : values()) {
            if (gender.getValue().equals(value)) {
                return gender;
            }
        }
        return UNKNOWN;
    }
}
