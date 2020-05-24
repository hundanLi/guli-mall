package com.hundanli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.common.utils.Utils;
import com.hundanli.gulimall.product.dao.SkuInfoDao;
import com.hundanli.gulimall.product.entity.SkuInfoEntity;
import com.hundanli.gulimall.product.service.SkuInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");
        if (Utils.isNumeric(brandId)) {
            long l = Long.parseLong(brandId);
            if (l > 0) {
                queryWrapper.eq("brand_id", l);
            }
        }
        if (Utils.isNumeric(catelogId)) {
            long l = Long.parseLong(catelogId);
            if (l>0) {
                queryWrapper.eq("catalog_id", l);
            }
        }
        min = min.substring(0, min.indexOf('.') > 0 ? min.indexOf('.') : min.length());
        if (Utils.isNumeric(min)) {
            queryWrapper.ge("price", new BigDecimal(min));
        }
        max = max.substring(0, max.indexOf('.')>0?max.indexOf('.'): max.length());
        if (Utils.isNumeric(max)) {
            BigDecimal decimal = new BigDecimal(max);
            if (decimal.compareTo(BigDecimal.ZERO) > 0) {
                queryWrapper.le("price", decimal);
            }
        }
        if (!StringUtils.isEmpty(key)) {
            if (Utils.isNumeric(key)) {
                queryWrapper.and(wrapper -> {
                    wrapper.eq("sku_id", Long.parseLong(key))
                            .or().like("sku_name", key);
                });
            } else {
                queryWrapper.like("sku_name", key);
            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper

        );
        return new PageUtils(page);

    }

}