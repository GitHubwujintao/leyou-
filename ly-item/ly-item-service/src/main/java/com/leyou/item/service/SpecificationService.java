package com.leyou.item.service;


import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecGroups(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        List<SpecParam>  list = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    public void saveSpecGroup(SpecGroup specGroup) {
        int result = specGroupMapper.insertSelective(specGroup);
        if (result == 0){
            throw  new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
    }

    public void deleteSpecGroupSparms(Long gid) {
       int result = specGroupMapper.deleteByPrimaryKey(gid);
       if (result == 0){
           throw  new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
       }
       SpecParam specParam = new SpecParam();
       specParam.setGroupId(gid);
       specParamMapper.delete(specParam);
    }
    public void updateSpecGroup(SpecGroup specGroup) {
        int count = specGroupMapper.updateByPrimaryKey(specGroup);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
    }

    public void saveSpecParams(SpecParam specParam) {
        int result = specParamMapper.insertSelective(specParam);
        if (result == 0){
            throw  new LyException(ExceptionEnum.SPEC_SAVE_ERROR);
        }
    }

    public void updateSpecParamByid(SpecParam specParam) {
        int count = specParamMapper.updateByPrimaryKeySelective(specParam);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
    }

    public void deleteSpecParamByid(Long pid) {
        int result = specParamMapper.deleteByPrimaryKey(pid);
        if (result == 0){
            throw  new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
    }
}
