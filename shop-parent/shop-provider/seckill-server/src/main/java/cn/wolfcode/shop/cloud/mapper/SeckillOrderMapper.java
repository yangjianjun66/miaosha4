package cn.wolfcode.shop.cloud.mapper;

import cn.wolfcode.shop.cloud.domain.OrderInfo;
import cn.wolfcode.shop.cloud.domain.SeckillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SeckillOrderMapper {
    @Select("select * from t_seckill_order where user_id = #{userId} and seckill_id = #{seckillId}")
    SeckillOrder finByUserIdAndSeckillId(@Param("userId") Long userId,@Param("seckillId") Long seckillId);

    @Insert("insert into t_seckill_order(user_id,seckill_id,order_no) values (#{userId},#{seckillId},#{orderNo})")
    void insert(SeckillOrder seckillOrder);

    @Select("select * from t_order_info where order_no = #{orderNo}")
    OrderInfo find(String orderNo);

    @Update("UPDATE t_order_info SET status = #{status},pay_date = now() WHERE order_no = #{orderNo} and status = 0")
    int changePayStatus(@Param("orderNo") String orderNo,@Param("status") Integer status);
}
