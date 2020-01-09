package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.service.GoodsService;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.vo.SpuBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        PageResult<SpuBo> result = this.goodsService.querySpuPage(page, rows, sortBy, desc, saleable, key);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        try {
            this.goodsService.save((SpuBo) spu);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("spu/detail/{pid}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("pid") Long pid) {
        SpuDetail detail = this.goodsService.querySpuDetailById(pid);
        if (detail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(detail);
    }
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkulBySpuId(@RequestParam("id") Long id) {
        List<Sku> skuList = this.goodsService.querySkulBySpuId(id);
        if (skuList == null || skuList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(skuList);
    }
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spu) {
        try {
            this.goodsService.updateGoods(spu);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
