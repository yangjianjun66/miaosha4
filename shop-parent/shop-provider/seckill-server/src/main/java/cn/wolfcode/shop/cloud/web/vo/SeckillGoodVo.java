package cn.wolfcode.shop.cloud.web.vo;

import cn.wolfcode.shop.cloud.domain.SeckillGood;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter@Getter
public class SeckillGoodVo extends SeckillGood implements Serializable {
    private String goodName;
    private String goodTitle;
    private String goodImg;
    private String goodDetail;
    private BigDecimal goodPrice;
}
