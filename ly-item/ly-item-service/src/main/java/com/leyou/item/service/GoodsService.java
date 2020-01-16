package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.pojo.Stock;
import com.leyou.vo.SpuBo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;


    public PageResult<SpuBo> querySpuPage(Integer page, Integer rows, String sortBy, Boolean desc, Boolean saleable, String key) {
        // 1、查询SPU
        // 分页,最多允许查100条
        PageHelper.startPage(page, Math.min(rows, 100));

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        // 默认排序
        example.setOrderByClause("last_update_time DESC");
        List<Spu> list = this.spuMapper.selectByExample(example);
        final PageInfo<Spu> pageInfo = new PageInfo<>(list);
        List<SpuBo> spus = new ArrayList<>();
        for (Spu spu:pageInfo.getList()) {
             SpuBo spuBo = new SpuBo();
            // 属性拷贝
              BeanUtils.copyProperties(spu, spuBo);
              List<String> names = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
              spuBo.setCname(StringUtils.join(names,"/"));
              spuBo.setBname(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
              spus.add(spuBo);
        }
        return new PageResult<>(pageInfo.getTotal(),spus);
    }
    @Transactional
    public void save(SpuBo spu) {
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);
        spu.getSpuDetail().setSpuId(spu.getId());
        spuDetailMapper.insert(spu.getSpuDetail());
        saveSkuAndStock(spu.getSkus(),spu.getId());
    }

    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        for (Sku sku:skus) {
            // 保存sku
            sku.setSpuId(spuId);
            // 初始化时间
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insert(sku);
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
        }
    }

    public SpuDetail querySpuDetailById(Long pid) {
        return this.spuDetailMapper.selectByPrimaryKey(pid);
    }


    public List<Sku> querySkulBySpuId(Long id) {
        Sku record = new Sku();
        record.setSpuId(id);
        List<Sku> skus = skuMapper.select(record);
        for (Sku sku : skus) {
            // 同时查询出库存
            sku.setStock(this.stockMapper.selectByPrimaryKey(sku.getId()).getStock());
        }
        return skus;
    }
    @Transactional
    public void updateGoods(SpuBo spu) {
        List<Sku> skuList = querySkulBySpuId(spu.getId());
        if (!CollectionUtils.isEmpty(skuList)){
            List<Long> skuids = skuList.stream().map(sku -> sku.getId()).collect(Collectors.toList());
            //删除以前库存
            stockMapper.deleteByIdList(skuids);
            //删除以前sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            skuMapper.delete(record);
        }
        // 新增sku和库存
        saveSkuAndStock(spu.getSkus(), spu.getId());
        //更行Spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);
        //更行商品详情
        spuDetailMapper.updateByPrimaryKey(spu.getSpuDetail());



    }
}
