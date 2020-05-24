package com.hundanli.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.common.utils.Utils;
import com.hundanli.gulimall.ware.dao.PurchaseDetailDao;
import com.hundanli.gulimall.ware.entity.PurchaseDetailEntity;
import com.hundanli.gulimall.ware.service.PurchaseDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            if (Utils.isNumeric(wareId)) {
                wrapper.eq("ware_id", Long.parseLong(wareId));
            }
        }
        if (!StringUtils.isEmpty(status)) {
            if (Utils.isNumeric(status)) {
                wrapper.eq("status", Integer.parseInt(status));
            }
        }
        if (!StringUtils.isEmpty(key)) {
            if (Utils.isNumeric(key)) {
                long l = Long.parseLong(key);
                wrapper.and(w -> {
                    w.eq("purchase_id", l)
                            .or().eq("sku_id", l);
                });
            }
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

}