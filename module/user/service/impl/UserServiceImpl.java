package top.wang.api.module.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wang.api.common.exception.ServerException;
import top.wang.api.module.user.convert.UserConvert;
import top.wang.api.module.user.mapper.UserMapper;
import top.wang.api.module.user.model.dto.UserDTO;
import top.wang.api.module.user.model.entity.UserEntity;
import top.wang.api.module.user.model.vo.UserVO;
import top.wang.api.module.user.service.UserService;

/**
 * 用户服务实现类
 *
 * @author mqxu
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserVO> page(Page<UserVO> page, String username) {
        // 构建查询条件
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(username), UserEntity::getUsername, username);
        wrapper.orderByDesc(UserEntity::getCreateTime);
        // 查询分页数据
        Page<UserEntity> entityPage = userMapper.selectPage(
                new Page<>(page.getCurrent(), page.getSize()), wrapper);
        // 使用 MapStruct 转换为 VO
        Page<UserVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(UserConvert.INSTANCE.convertToVOList(entityPage.getRecords()));

        return voPage;
    }

    @Override
    public UserVO getById(Long id) {
        UserEntity entity = userMapper.selectById(id);
        if (entity == null) {
            throw new ServerException("用户不存在");
        }
        return UserConvert.INSTANCE.convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserDTO dto) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserEntity::getUsername, dto.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new ServerException("用户名已存在");
        }

        // 使用 MapStruct 转换为实体
        UserEntity entity = UserConvert.INSTANCE.convertToEntity(dto);
        // 密码加密
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        userMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDTO dto) {
        // 检查用户是否存在
        UserEntity existEntity = userMapper.selectById(dto.getId());
        if (existEntity == null) {
            throw new ServerException("用户不存在");
        }

        // 使用 MapStruct 转换
        UserEntity entity = UserConvert.INSTANCE.convertToEntity(dto);
        // 如果密码不为空，则加密更新；否则保留原密码
        if (StrUtil.isNotBlank(dto.getPassword())) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            entity.setPassword(existEntity.getPassword());
        }

        userMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(UserDTO dto, Long userId) {
        // 检查用户是否存在
        UserEntity existEntity = userMapper.selectById(userId);
        if (existEntity == null) {
            throw new ServerException("用户不存在");
        }

        // 只允许修改昵称、头像、性别、真实姓名
        UserEntity entity = new UserEntity();
        entity.setId(userId);

        if (StrUtil.isNotBlank(dto.getNickname())) {
            entity.setNickname(dto.getNickname());
        }
        if (StrUtil.isNotBlank(dto.getAvatar())) {
            entity.setAvatar(dto.getAvatar());
        }
        if (dto.getGender() != null) {
            entity.setGender(dto.getGender());
        }
        if (StrUtil.isNotBlank(dto.getRealName())) {
            entity.setRealName(dto.getRealName());
        }

        userMapper.updateById(entity);
    }
}
