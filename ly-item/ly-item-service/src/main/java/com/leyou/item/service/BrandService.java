package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public  PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);

        //条件过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotEmpty(key)){
            example.createCriteria().orLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
        if (StringUtils.isNoneBlank(sortBy)){
            String orderByClause = sortBy + (desc ? "DESC":"ASC");
            example.setOrderByClause(sortBy);
        }
        List<Brand> list = brandMapper.selectByExample(example);

        PageInfo<Brand> results = new PageInfo<>(list);
        return new PageResult<>(results.getTotal(),list);
    }

    public void saveBrand(Brand brand, List<Long> cids) {

        brandMapper.insertSelective(brand);
        for (Long cid: cids) {
        brandMapper.insertCategoryBrand(cid,brand.getId());

        }
    }
}