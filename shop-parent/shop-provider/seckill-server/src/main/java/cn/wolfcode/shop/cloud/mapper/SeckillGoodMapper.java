package cn.wolfcode.shop.cloud.mapper;

import cn.wolfcode.shop.cloud.domain.SeckillGood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SeckillGoodMapper {

    @Select("select * from t_seckill_goods")
    List<SeckillGood> query();

    @Select("select * from t_seckill_goods where id = #{seckillId}")
    SeckillGood get(Long seckillId);

    @Update("update t_seckill_goods set stock_count = stock_count -1 where id = #{seckillId} and stock_count > 0")
    int doSeckill(Long seckillId);

    @Select("select stock_count from t_seckill_goods where id = #{seckillId}")
    int getStockCount(Long seckillId);

    @Update("update t_seckill_goods set stock_count = stock_count + 1 where id = #{seckillId}")
    void incrStock(Long seckillId);
}
