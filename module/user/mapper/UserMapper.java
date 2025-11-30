package top.wang.api.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.wang.api.module.user.model.entity.UserEntity;

/**
 * 用户 Mapper
 *
 * @author mqxu
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
