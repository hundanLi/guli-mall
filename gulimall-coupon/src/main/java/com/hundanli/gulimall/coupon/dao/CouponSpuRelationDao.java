package com.hundanli.gulimall.coupon.dao;

import com.hundanli.gulimall.coupon.entity.CouponSpuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与产品关联
 * 
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 13:56:10
 */
@Mapper
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {
	
}
