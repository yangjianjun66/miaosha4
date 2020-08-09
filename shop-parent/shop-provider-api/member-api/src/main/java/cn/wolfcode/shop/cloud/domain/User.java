package cn.wolfcode.shop.cloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class User {
    private Long id;
    private String nickname;//昵称
    @JsonIgnore
    private String head;//头像
    @JsonIgnore
    private String password;//密码
    @JsonIgnore
    private String salt;//盐
    @JsonIgnore
    private Date registerDate;//注册时间
    @JsonIgnore
    private Date lastLoginDate;//最后登录时间
    @JsonIgnore
    private Integer loginCount;//登录次数
}
