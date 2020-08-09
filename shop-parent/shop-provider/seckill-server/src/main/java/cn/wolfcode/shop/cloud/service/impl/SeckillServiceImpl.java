package cn.wolfcode.shop.cloud.service.impl;

import cn.wolfcode.shop.cloud.common.BussinessException;
import cn.wolfcode.shop.cloud.common.Result;
import cn.wolfcode.shop.cloud.domain.Good;
import cn.wolfcode.shop.cloud.domain.SeckillGood;
import cn.wolfcode.shop.cloud.mq.MQConstants;
import cn.wolfcode.shop.cloud.redis.MyRedisTemplate;
import cn.wolfcode.shop.cloud.redis.SeckillKeyPrefix;
import cn.wolfcode.shop.cloud.web.feign.GoodFeignApi;
import cn.wolfcode.shop.cloud.mapper.SeckillGoodMapper;
import cn.wolfcode.shop.cloud.msg.SeckillCodeMsg;
import cn.wolfcode.shop.cloud.service.ISeckillService;
import cn.wolfcode.shop.cloud.web.vo.SeckillGoodVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SeckillServiceImpl implements ISeckillService {
    @Autowired
    private SeckillGoodMapper seckillGoodMapper;
    @Autowired
    private GoodFeignApi goodFeignApi;
    @Autowired
    private MyRedisTemplate myRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public List<SeckillGoodVo> query() {
        //首先查询出SeckillGood里面的所有的数据
        List<SeckillGood> seckillGoodList = seckillGoodMapper.query();
        List<SeckillGoodVo> seckillGoodVoList = getSeckillGoodVos(seckillGoodList);
        return seckillGoodVoList;
    }

    private List<SeckillGoodVo> getSeckillGoodVos(List<SeckillGood> seckillGoodList) {
        Set<Long> idset = new HashSet<>();
        //遍历这个放到新的集合里,目的是为了去重
        for (SeckillGood sg : seckillGoodList) {
            idset.add(sg.getGoodId());
        }
        List<Long> ids = new ArrayList(idset);
        Result<List<Good>> result = goodFeignApi.queryByIds(ids);
        for (Good good : result.getData()) {
            System.out.println("====>"+good.getGoodName());
        }
        if (result == null || result.hasError()){
            throw new BussinessException(SeckillCodeMsg.PRODUCT_SERVER_ERROR);
        }
        List<Good> goodList = result.getData();
        //这里使用Map的目的就是为了通过id去查询另外一个表的数据
        Map<Long, Good> goodMap = new HashMap<>();
        for (Good good : goodList) {
            goodMap.put(good.getId(),good);
        }
        List<SeckillGoodVo> seckillGoodVoList = new ArrayList<>();
        for (SeckillGood sg : seckillGoodList) {
            Good good = goodMap.get(sg.getGoodId());
            SeckillGoodVo vo = new SeckillGoodVo();
            vo.setGoodDetail(good.getGoodDetail());
            vo.setGoodImg(good.getGoodImg());
            vo.setGoodName(good.getGoodName());
            vo.setGoodPrice(good.getGoodPrice());
            vo.setGoodTitle(good.getGoodTitle());
            vo.setEndDate(sg.getEndDate());
            vo.setGoodId(good.getId());
            vo.setId(sg.getId());
            vo.setSeckillPrice(sg.getSeckillPrice());
            vo.setStartDate(sg.getStartDate());
            vo.setStockCount(sg.getStockCount());
            seckillGoodVoList.add(vo);
        }
        return seckillGoodVoList;
    }

    @Override
    public SeckillGoodVo find(Long seckillId) {
        if (seckillId==null){
            return null;
        }
        SeckillGood sg = seckillGoodMapper.get(seckillId);
        if (sg==null){
            return null;
        }
        List<SeckillGood> seckillGoodList = new ArrayList<>();
        seckillGoodList.add(sg);
        List<SeckillGoodVo> seckillGoodVoList = getSeckillGoodVos(seckillGoodList);
        return seckillGoodVoList.get(0);
    }

    @Override
    public List<SeckillGoodVo> queryByCache() {
        Map<String,SeckillGoodVo> resultMap = myRedisTemplate.hgetAll(SeckillKeyPrefix.SECKILL_GOOD_HASH,"",SeckillGoodVo.class);
        return new ArrayList<>(resultMap.values());
    }

    @Override
    public SeckillGoodVo findByCache(Long seckillId) {
        SeckillGoodVo vo = myRedisTemplate.hget(SeckillKeyPrefix.SECKILL_GOOD_HASH, "", seckillId + "", SeckillGoodVo.class);
        return vo;
    }

    @Override
    public int doSeckill(Long seckillId) {
       return seckillGoodMapper.doSeckill(seckillId);
    }

    @Override
    public void syncRedisStockCount(Long seckillId) {
        int stockCount = seckillGoodMapper.getStockCount(seckillId);
        if (stockCount>0){
            //先发布订阅模式提醒取消本地标识
            rabbitTemplate.convertAndSend(MQConstants.SECKILL_OVER_SIGN_PUBSUB_EX,"",seckillId);
            myRedisTemplate.set(SeckillKeyPrefix.SECKILL_STOCK_COUNT,seckillId+"",stockCount);
        }
    }

    @Override
    public void incrStock(Long seckillId) {
        seckillGoodMapper.incrStock(seckillId);
    }
}
