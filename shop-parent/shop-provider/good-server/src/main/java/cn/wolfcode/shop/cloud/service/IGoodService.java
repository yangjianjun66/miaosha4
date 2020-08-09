package cn.wolfcode.shop.cloud.service;

import cn.wolfcode.shop.cloud.domain.Good;

import java.util.List;

public interface IGoodService {
    List<Good> queryByIds(List<Long> ids);
}
