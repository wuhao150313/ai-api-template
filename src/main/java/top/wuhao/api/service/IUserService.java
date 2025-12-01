package top.wuhao.api.service;

import top.wuhao.api.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.wuhao.api.entity.dto.LoginDTO;
import top.wuhao.api.entity.vo.TokenVO;
import top.wuhao.api.entity.vo.UserInfoVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wang
 * @since 2025-11-19
 */
public interface IUserService extends IService<User> {

    /**
     * 账号密码登录
     *
     * @param dto 登录信息
     * @return Token
     */
    TokenVO login(LoginDTO dto);

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
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    UserInfoVO getCurrentUserInfo();

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfoById(Long userId);
}
