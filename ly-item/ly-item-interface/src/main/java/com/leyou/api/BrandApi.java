package com.leyou.api;


import com.leyou.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



public interface BrandApi {

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @PostMapping
    // 传入 "1,2,3"的字符串可以解析为列表
     void saveBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    /**
     * 根据分类查询品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    List<Brand> queryBrandByCategory(@PathVariable("cid") Long cid);


    /**
     * 根据id查询品牌
     * @param id
     */
    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);

    /**
     * 根据id列表查询品牌列表
     * @param ids
     * @return
     */
    @GetMapping("brand/list")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);
}
