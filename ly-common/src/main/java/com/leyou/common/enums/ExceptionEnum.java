package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 异常信息定义-枚举
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    BRAND_NOT_FOUND(404,"品牌不存在！"),
    CATEGORY_NOT_FOUND(404,"商品分类未找到！"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格组未找到！"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数未找到！"),
    GOODS_NOT_FOUND(404,"商品不存在！"),
    GOODS_DETAIL_NOT_FOUND(404,"商品详情不存在！"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在！"),
    GOODS_STOCK_NOT_FOUND(404,"商品STOCK不存在！"),
    SEARCH_NOT_FOUND(404,"搜索结果不存在！"),
    CARTS_NOT_FOUND(404,"购物车为空！"),
    ORDER_NOT_FOUND(404,"订单不存在！"),
    ORDER_DETAIL_NOT_FOUND(404,"订单详情不存在！"),
    ORDER_STATUS_NOT_FOUND(404,"订单状态不存在！"),
    BRAND_SAVE_ERROR(500,"新增品牌失败！"),
    GOODS_SAVE_ERROR(500,"新增商品失败！"),
    BRAND_UPDATE_ERROR(500,"修改品牌失败！"),
    GOODS_UPDATE_ERROR(500,"修改商品失败！"),
    UPLOAD_FILE_ERROR(500,"上传图片失败！"),
    DELETE_BRAND_EXCEPTION(500,"删除品牌失败！"),
    CREATE_ORDER_ERROR(500,"创建订单失败！"),
    CREATE_ORDER_DETAIL_ERROR(500, "创建订单详情失败！"),
    CREATE_ORDER_STATUS_ERROR(500, "创建订单详情失败！"),
    STOCK_NOT_ENOUGH(500, "库存不足！"),
    WX_PAY_ORDER_FAIL(500, "微信下单失败！"),
    ORDER_STATUS_ERROR(500, "订单状态错误！"),
    UPDATE_ORDER_STATUS_ERROR(500, "更新订单状态错误！"),
    INVALID_FILE_TYPE(400,"无效文件类型！"),
    GOODS_ID_CANNOT_BE_NULL(400,"无效文件类型！"),
    INVALID_USER_DATA_TYPE(400,"无效用户参数类型！"),
    INVALID_SIGN_ERROR(400,"无效的签名异常！"),
    INVALID_ORDER_PARAM(400,"订单参数异常！"),
    UNAUTHORIZED(403,"未授权！")

    ;
    private int code;
    private String msg;
}
