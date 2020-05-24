package com.hundanli.gulimall.product.dao;

import com.hundanli.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
