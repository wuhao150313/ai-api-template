package top.wang.api.module.user.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.wang.api.module.user.model.dto.UserDTO;
import top.wang.api.module.user.model.entity.UserEntity;
import top.wang.api.module.user.model.vo.UserVO;

import java.util.List;

/**
 * 用户对象转换接口
 *
 * @author mqxu
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * Entity 转 VO
     */
    UserVO convertToVO(UserEntity entity);

    /**
     * Entity 列表转 VO 列表
     */
    List<UserVO> convertToVOList(List<UserEntity> entityList);

    /**
     * DTO 转 Entity
     */
    UserEntity convertToEntity(UserDTO dto);
}
