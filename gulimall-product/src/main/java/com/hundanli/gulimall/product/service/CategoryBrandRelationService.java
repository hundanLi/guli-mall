package com.hundanli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.product.entity.BrandEntity;
import com.hundanli.gulimall.product.entity.CategoryBrandRelationEntity;
import com.hundanli.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void saveRelation(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrandName(BrandEntity brand);

    void updateCategoryName(CategoryEntity category);

    List<BrandEntity> getBrandList(Long catId);
}

