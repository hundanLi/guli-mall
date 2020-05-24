package com.hundanli.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.ware.entity.PurchaseDetailEntity;
import com.hundanli.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 14:46:02
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStocks(List<PurchaseDetailEntity> detailEntityList);
}

