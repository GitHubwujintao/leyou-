package com.leyou.item.mapper;

import com.leyou.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface StockMapper extends Mapper<Stock>, DeleteByIdListMapper<Stock, Long> {
}
