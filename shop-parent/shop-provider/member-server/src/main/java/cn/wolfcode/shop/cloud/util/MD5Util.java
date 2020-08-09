package cn.wolfcode.shop.cloud.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * Created by wolfcode-lanxw
 */
public class MD5Util {
    public static String encode(String password,String salt){
        return DigestUtils.md5Hex(""+salt.charAt(0)+salt.charAt(2)+password+salt.charAt(4)+salt.charAt(5));
    }

    public static void main(String[] args) {
        String salt = UUID.randomUUID().toString().replace("-","").substring(0,6);
        System.out.println(salt);
        System.out.println(MD5Util.encode("111111",salt));
    }
}
