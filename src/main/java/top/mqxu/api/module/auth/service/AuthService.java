package top.mqxu.api.module.auth.service;

import top.mqxu.api.module.auth.model.dto.LoginDTO;
import top.mqxu.api.module.auth.model.dto.SmsLoginDTO;
import top.mqxu.api.module.auth.model.vo.TokenVO;
import top.mqxu.api.module.auth.model.dto.WechatLoginDTO;

/**
 * 认证服务接口
 *
 * @author mqxu
 */
public interface AuthService {

    /**
     * 账号密码登录
     *
     * @param dto 登录信息
     * @return Token
     */
    TokenVO login(LoginDTO dto);

    /**
     * 短信验证码登录
     *
     * @param dto 登录信息
     * @return Token
     */
    TokenVO smsLogin(SmsLoginDTO dto);

    /**
     * 微信登录
     *
     * @param dto 登录信息
     * @return Token
     */
    TokenVO wechatLogin(WechatLoginDTO dto);

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     */
    void sendSmsCode(String mobile);

    /**
     * 登出
     *
     * @param token JWT Token
     */
    void logout(String token);

    /**
     * 根据用户 ID 登出
     *
     * @param userId 用户 ID
     */
    void logoutByUserId(Long userId);

    /**
     * 绑定手机号
     *
     * @param mobile 手机号
     * @param code   验证码
     * @param userId 用户 ID
     */
    void bindMobile(String mobile, String code, Long userId);

    /**
     * 换绑手机号
     *
     * @param oldMobile 旧手机号
     * @param oldCode   旧手机验证码
     * @param newMobile 新手机号
     * @param newCode   新手机验证码
     * @param userId    用户 ID
     */
    void changeMobile(String oldMobile, String oldCode, String newMobile, String newCode, Long userId);
}
