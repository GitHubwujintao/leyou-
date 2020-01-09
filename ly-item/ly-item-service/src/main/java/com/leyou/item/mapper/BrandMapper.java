package com.leyou.item.mapper;

import com.leyou.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid,@Param("bid") Long bid);
    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    int deleteCategoryBrand(@Param("bid") Long bid);
    @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id = #{cid}) ")
    List<Brand> selectBrandByCategoryByid(@Param("cid") Long cid);

}
