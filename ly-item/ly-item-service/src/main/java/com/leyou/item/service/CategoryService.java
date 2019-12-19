package com.leyou.item.service;


import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    public List<Category> queryCategoryListByPid(Long pid) {
         Category category = new Category();
         category.setParentId(pid);
         List<Category> categoryList = categoryMapper.select(category);
         if (CollectionUtils.isEmpty(categoryList)){
             throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
         }
         return categoryList;
    }
}
