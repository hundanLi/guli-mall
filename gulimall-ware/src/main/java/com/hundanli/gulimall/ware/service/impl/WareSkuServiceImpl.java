package com.hundanli.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.common.utils.R;
import com.hundanli.common.utils.Utils;
import com.hundanli.gulimall.ware.dao.WareSkuDao;
import com.hundanli.gulimall.ware.entity.PurchaseDetailEntity;
import com.hundanli.gulimall.ware.entity.WareSkuEntity;
import com.hundanli.gulimall.ware.feign.ProductFeignService;
import com.hundanli.gulimall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * @author li
 */
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");

        if (!StringUtils.isEmpty(skuId)) {
            if (Utils.isNumeric(skuId)) {
                queryWrapper.eq("sku_id", Long.parseLong(skuId));
            }
        }
        if (!StringUtils.isEmpty(wareId)) {
            if (Utils.isNumeric(wareId)) {
                queryWrapper.eq("ware_id", Long.parseLong(wareId));
            }
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addStocks(List<PurchaseDetailEntity> detailEntityList) {
        detailEntityList.forEach(item -> {
            Long skuId = item.getSkuId();
            Integer skuNum = item.getSkuNum();
            Long wareId = item.getWareId();

            WareSkuEntity wareSkuEntity = this.getOne(
                    new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId)
                            .eq("ware_id", wareId));
            if (wareSkuEntity == null) {
                wareSkuEntity = new WareSkuEntity();
                wareSkuEntity.setSkuId(skuId);
                wareSkuEntity.setStock(skuNum);
                wareSkuEntity.setWareId(wareId);
                wareSkuEntity.setStockLocked(0);
                // 远程调用，获取skuName
                try {
                    R r = productFeignService.info(skuId);
                    if (r.getCode() == 0) {
                        //noinspection unchecked
                        Map<String, Object> skuInfo = (Map<String, Object>) r.get("skuInfo");
                        String skuName = (String) skuInfo.get("skuName");
                        wareSkuEntity.setSkuName(skuName);
                    }
                } catch (Exception e) {
                    log.error("远程调用失败：", e);
                }
                this.save(wareSkuEntity);
            }else {
                wareSkuEntity.setStock(wareSkuEntity.getStock() + skuNum);
                this.updateById(wareSkuEntity);
            }

        });
    }

}