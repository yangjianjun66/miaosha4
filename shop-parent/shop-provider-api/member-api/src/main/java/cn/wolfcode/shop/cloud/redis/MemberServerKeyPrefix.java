package cn.wolfcode.shop.cloud.redis;

public class MemberServerKeyPrefix implements KeyPrefix{
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
    private MemberServerKeyPrefix(String prefix,int expireSeconds){
        this.prefix = prefix;
        this.expireSeconds = expireSeconds;
    }
    public static final MemberServerKeyPrefix USER_TOKEN = new MemberServerKeyPrefix("userToken",30*60);
}
