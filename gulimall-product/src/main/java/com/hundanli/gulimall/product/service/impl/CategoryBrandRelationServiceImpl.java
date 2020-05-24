package com.hundanli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.gulimall.product.dao.CategoryBrandRelationDao;
import com.hundanli.gulimall.product.entity.BrandEntity;
import com.hundanli.gulimall.product.entity.CategoryBrandRelationEntity;
import com.hundanli.gulimall.product.entity.CategoryEntity;
import com.hundanli.gulimall.product.service.BrandService;
import com.hundanli.gulimall.product.service.CategoryBrandRelationService;
import com.hundanli.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveRelation(CategoryBrandRelationEntity categoryBrandRelation) {
        // 查出brandName和catelogName
        BrandEntity brandEntity = brandService.getById(categoryBrandRelation.getBrandId());
        CategoryEntity categoryEntity = categoryService.getById(categoryBrandRelation.getCatelogId());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        super.save(categoryBrandRelation);
    }

    @Override
    public void updateBrandName(BrandEntity brand) {
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("brand_id", brand.getBrandId());
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandId(brand.getBrandId());
        relationEntity.setBrandName(brand.getName());
        this.update(relationEntity, updateWrapper);
    }

    @Override
    public void updateCategoryName(CategoryEntity category) {
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("catelog_id", category.getCatId());
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setCatelogName(category.getName());
        relationEntity.setCatelogId(category.getCatId());
        this.update(relationEntity, updateWrapper);
    }

    @Override
    public List<BrandEntity> getBrandList(Long catId) {
        // 1.分类与品牌关联列表
        QueryWrapper<CategoryBrandRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id", catId);
        List<CategoryBrandRelationEntity> relationEntityList = this.list(queryWrapper);
        // 2.品牌列表
        List<Long> brandIds = relationEntityList.stream().map(CategoryBrandRelationEntity::getBrandId).collect(Collectors.toList());
        return brandService.listByIds(brandIds);
    }

}