package com.hundanli.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.to.SkuReductionTo;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.gulimall.coupon.dao.SkuFullReductionDao;
import com.hundanli.gulimall.coupon.entity.MemberPriceEntity;
import com.hundanli.gulimall.coupon.entity.SkuFullReductionEntity;
import com.hundanli.gulimall.coupon.entity.SkuLadderEntity;
import com.hundanli.gulimall.coupon.service.MemberPriceService;
import com.hundanli.gulimall.coupon.service.SkuFullReductionService;
import com.hundanli.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        //1.sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTo, skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuLadderEntity.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        //2.sms_sku_full_reduction
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, reductionEntity);
        if (reductionEntity.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
            this.save(reductionEntity);
        }

        //3.sms_member_price
        List<MemberPriceEntity> priceEntityList = skuReductionTo.getMemberPrice().stream().map(item -> {
            MemberPriceEntity priceEntity = new MemberPriceEntity();
            priceEntity.setMemberLevelId(item.getId());
            priceEntity.setMemberLevelName(item.getName());
            priceEntity.setMemberPrice(item.getPrice());
            priceEntity.setSkuId(skuReductionTo.getSkuId());
            priceEntity.setAddOther(1);
            return priceEntity;
        }).filter(item -> {
            return item.getMemberPrice().compareTo(BigDecimal.ZERO) > 0;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(priceEntityList);
    }

}