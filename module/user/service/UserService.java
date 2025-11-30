package top.wang.api.module.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.wang.api.module.user.model.dto.UserDTO;
import top.wang.api.module.user.model.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author mqxu
 */
public interface UserService {

    /**
     * 分页查询用户列表
     *
     * @param page     分页参数
     * @param username 用户名（可选）
     * @return 用户列表
     */
    Page<UserVO> page(Page<UserVO> page, String username);

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    UserVO getById(Long id);

    /**
     * 新增用户
     *
     * @param dto 用户信息
     */
    void save(UserDTO dto);

    /**
     * 更新用户
     *
     * @param dto 用户信息
     */
    void update(UserDTO dto);

    /**
     * 删除用户
     *
     * @param id 用户 ID
     */
    void delete(Long id);

    /**
     * 修改个人信息
     *
     * @param dto    用户信息
     * @param userId 用户 ID
     */
    void updateProfile(UserDTO dto, Long userId);
}
