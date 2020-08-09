package cn.wolfcode.shop.cloud.redis;

public class SeckillKeyPrefix implements KeyPrefix {
    private String prefix;
    private int expireSeconds;
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public int getExprieSeconds() {
        return expireSeconds;
    }
    private SeckillKeyPrefix(String prefix,int expireSeconds){
        this.prefix = prefix;
        this.expireSeconds = expireSeconds;
    }
    public static SeckillKeyPrefix SECKILL_STOCK_COUNT= new SeckillKeyPrefix("seckillCount:",-1);
    public static SeckillKeyPrefix SECKILL_ORDER= new SeckillKeyPrefix("seckillOrder:",-1);
    public static SeckillKeyPrefix SECKILL_GOOD_HASH= new SeckillKeyPrefix("seckillGoodHash:",-1);
    public static SeckillKeyPrefix SECKILL_PATH= new SeckillKeyPrefix("seckillPath:",2);
    public static SeckillKeyPrefix VERIFY_CODE_RESULT= new SeckillKeyPrefix("verifyCodeResult",3*60);
}
