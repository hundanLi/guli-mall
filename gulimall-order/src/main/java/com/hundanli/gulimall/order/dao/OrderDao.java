package com.hundanli.gulimall.order.dao;

import com.hundanli.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 14:20:07
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
