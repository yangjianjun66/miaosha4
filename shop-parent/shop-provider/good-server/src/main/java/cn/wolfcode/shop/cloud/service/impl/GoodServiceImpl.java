package cn.wolfcode.shop.cloud.service.impl;

import cn.wolfcode.shop.cloud.domain.Good;
import cn.wolfcode.shop.cloud.mapper.GoodMapper;
import cn.wolfcode.shop.cloud.service.IGoodService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class GoodServiceImpl implements IGoodService {
    @Autowired
    private GoodMapper goodMapper;
    @Override
    public List<Good> queryByIds(List<Long> ids) {
        if (ids == null || ids.size()==0){
            return Collections.EMPTY_LIST;
        }
        for (Long id : ids) {
            System.out.println(id);
        }
        System.err.println("GoodServiceImpl执行了");
        List<Good> goodList = goodMapper.queryByIds(ids);
        for (Good good : goodList) {
            System.out.println(good.getGoodName());
        }
        return goodList;
    }

}
