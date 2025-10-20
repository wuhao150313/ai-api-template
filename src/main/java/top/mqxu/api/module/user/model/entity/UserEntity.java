package top.mqxu.api.module.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.mqxu.api.common.entity.BaseEntity;

/**
 * 用户实体类
 *
 * @author mqxu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "用户实体")
public class UserEntity extends BaseEntity {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别（0-未知，1-男，2-女）")
    private Integer gender;

    @Schema(description = "微信 openid")
    private String wxOpenid;

    @Schema(description = "状态（0-禁用，1-正常）")
    private Integer status;
}
