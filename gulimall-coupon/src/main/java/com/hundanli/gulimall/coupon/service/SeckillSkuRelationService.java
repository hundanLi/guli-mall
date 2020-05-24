package com.hundanli.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.coupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 13:56:10
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

