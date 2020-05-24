package com.hundanli.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.coupon.entity.CouponEntity;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 13:56:10
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

