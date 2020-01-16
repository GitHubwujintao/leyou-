package com.leyou.item.web;


import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.service.BrandService;
import com.leyou.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value="page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer row,
            @RequestParam(value = "sortBy",required =false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false) String key){

        PageResult<Brand>  result = brandService.queryBrandByPageAndSort(page,row,sortBy,desc,key);
        System.out.println(result.getItems().size());
        if (result == null || result.getItems().size() == 0) {
            return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand,@RequestParam(value = "cids") List<Long> cids){
        brandService.saveBrand(brand,cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<Void> updateBrandByid(Brand brand,@RequestParam(value = "cids") List<Long> cids){
        brandService.updateBrand(brand,cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("delete")
    public ResponseEntity<Void> deleteBrandByid(@RequestParam(value = "id") Long id){
        brandService.deleteBrandByid(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> selectBrandByCategoryByid(@PathVariable(value = "cid") Long cid){
        List<Brand> brandList= brandService.selectBrandByCategoryByid(cid);
        if(brandList == null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(brandList);
    }
    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(brandService.queryById(id));
    }
    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }

}
