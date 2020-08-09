package cn.wolfcode.shop.cloud.mapper;

import cn.wolfcode.shop.cloud.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderInfoMapper {
    @Insert("insert into t_order_info " +
            "(order_no,user_id,good_id,good_img,delivery_addr_id,good_name,good_count,good_price,seckill_price,status,create_date,pay_date)" +
            " values " +
            "(#{orderNo},#{userId},#{goodId},#{goodImg},#{deliveryAddrId},#{goodName},#{goodCount},#{goodPrice},#{seckillPrice},#{status},#{createDate},#{payDate})")
    void insert(OrderInfo orderInfo);

    @Select("select * from t_order_info where order_no = #{orderNo}")
    OrderInfo find(String orderNo);

    @Update("update t_order_info set status = #{status} where order_no = #{orderNo}")
    int updateCancelStatus(@Param("orderNo") String orderNo,@Param("status") Integer status);
}
