package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SearchService {
    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private ElasticsearchTemplate template;

    public Goods buildGoods(Spu spu) {
        // 查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        List<String> categorytNames = categoryClient.queryNameByCids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 1、准备数据
        // sku集合
        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
        // 处理sku
        Set<Long> prices = new HashSet<>();
        List<Map<String,Object>> skuList = new ArrayList<>();
        for (Sku sku : skus) {
            prices.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            map.put("price", sku.getPrice());
            skuList.add(map);
        }
        //处理规格参数
        // 查询规格参数
        List<SpecParam> specParams = specificationClient.querySpecParams(null, spu.getCid3(), true, null);
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {});
        //自定义spec对应的map
        Map<String, Object> specs = new HashMap<>();
        //对规格进行遍历，并封装spec，其中spec的key是规格参数的名称，值是商品详情中的值
        for (SpecParam specParam : specParams) {
            String key = specParam.getName();
            Object value = " ";
            if (specParam.getGeneric()){
                //参数是通用属性，通过规格参数的ID从商品详情存储的规格参数中查出值
                value = genericSpec.get(specParam.getId());
                if (specParam.getNumeric()){
                    //参数是数值类型，处理成段，方便后期对数值类型进行范围过滤
                    value = chooseSegment(value.toString(), specParam);
                }
            } else {
                //参数不是通用类型
                value = specialSpec.get(specParam.getId());
            }
            value = (value == null ? "其他" : value);
            //存入map
            specs.put(key, value);
        }

        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setAll(spu.getTitle()+" " +StringUtils.join(categorytNames," ")+brand.getName());
        goods.setPrice(prices);  //一个spu下sku的价格集合
        goods.setSkus(JsonUtils.serialize(skuList));       //一个spu下所有sku的集合
        goods.setSubTitle(spu.getSubTitle());
        goods.setSpecs(specs);     //通用规格参数特有规格参数
        goods.setCreateTime(spu.getCreateTime());
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)){
            return null;
        }
        Integer page = request.getPage() - 1;// page 从0开始
        Integer size = request.getSize();
        // 1、创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 2、查询
        // 2.1、对结果进行筛选
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));
        // 2.2、基本查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", key));
        //// 2.3、分页
        queryBuilder.withPageable(PageRequest.of(page,size));
        // 3、返回结果
        Page<Goods> result = this.repository.search(queryBuilder.build());
        // 4、解析结果
        long total = result.getTotalElements();
        long totalPage = (total + size - 1) / size;
       return new PageResult<Goods>(total, (int)totalPage, result.getContent());
    }
}