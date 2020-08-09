package cn.wolfcode.shop.cloud.mapper;

import cn.wolfcode.shop.cloud.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User selectByPrimaryKey(Long id);
}
