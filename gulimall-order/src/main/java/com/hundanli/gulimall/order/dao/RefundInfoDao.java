package com.hundanli.gulimall.order.dao;

import com.hundanli.gulimall.order.entity.RefundInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款信息
 * 
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 14:20:07
 */
@Mapper
public interface RefundInfoDao extends BaseMapper<RefundInfoEntity> {
	
}
