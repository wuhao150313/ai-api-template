package top.mqxu.api.infrastructure.wechat;

import lombok.Data;

/**
 * 微信用户信息
 *
 * @author mqxu
 */
@Data
public class WechatUserInfo {

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符（需要开发者拥有多个移动应用、网站应用和公众帐号）
     */
    private String unionid;

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
