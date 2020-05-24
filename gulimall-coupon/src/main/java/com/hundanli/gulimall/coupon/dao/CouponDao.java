package com.hundanli.gulimall.coupon.dao;

import com.hundanli.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 13:56:10
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
