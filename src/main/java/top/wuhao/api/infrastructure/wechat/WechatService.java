package top.wuhao.api.infrastructure.wechat;

/**
 * 微信服务接口
 *
 * @author mqxu
 */
public interface WechatService {

    /**
     * 通过 code 获取微信用户的 openid 和 session_key
     *
     * @param code 微信登录 code
     * @return 微信用户信息
     */
    WechatUserInfo getUserInfo(String code);
}
