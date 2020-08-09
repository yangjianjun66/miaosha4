package cn.wolfcode.shop.cloud.mapper;

import cn.wolfcode.shop.cloud.domain.Good;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GoodMapper {
    @SelectProvider(type = GoodMapperSQLProvider.class,method = "queryByIds")
    List<Good> queryByIds(@Param("ids")List<Long> ids);
    class GoodMapperSQLProvider{
        public String queryByIds(@Param("ids")List<Long> ids){
            StringBuilder sb = new StringBuilder();
            sb.append("select * from t_goods");
            if (ids != null && ids.size()>0){
                sb.append(" where id in (");
                for (int i=0;i<ids.size();i++){
                    if (i!=0){
                        sb.append(",");
                    }
                    sb.append(ids.get(i));
                }
                sb.append(")");
            }
            return sb.toString();
        }
    }
}
