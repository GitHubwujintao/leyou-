package com.leyou.item.web;

import com.leyou.item.service.SpecificationService;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecGroups(cid);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
    @PostMapping("group")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroup specGroup){
        specificationService.saveSpecGroup(specGroup);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("group")
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroup specGroup){
        specificationService.updateSpecGroup(specGroup);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("group/{gid}")
    public ResponseEntity<Void> deleteSpecGroupSparms(@PathVariable("gid") Long gid){
        specificationService.deleteSpecGroupSparms(gid);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /*
    * 查询SpecParam商品组参数*/
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParams(@RequestParam(value = "gid",required = false) Long gid,
                                                           @RequestParam(value = "cid",required = false) Long cid,
                                                           @RequestParam(value = "searching",required = false) Boolean searching,
                                                           @RequestParam(value="generic", required = false) Boolean generic){
        List<SpecParam> list = this.specificationService.querySpecParams(gid,cid,searching,generic);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
    /*
    添加SpecParam商品组参数
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveSpecParams(@RequestBody SpecParam specParam){
        this.specificationService.saveSpecParams(specParam);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("param")
    public ResponseEntity<Void> updateSpecParamByid(@RequestBody SpecParam specParam){
        specificationService.updateSpecParamByid(specParam);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("param/{pid}")
    public ResponseEntity<Void> deleteSpecParamByid(@PathVariable("pid") Long pid){
        specificationService.deleteSpecParamByid(pid);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}