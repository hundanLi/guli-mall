package com.hundanli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.gulimall.product.dao.CategoryDao;
import com.hundanli.gulimall.product.entity.CategoryEntity;
import com.hundanli.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        // TODO 检查是否有其他地方引用
        return super.removeByIds(idList);
    }

    @Override
    public List<CategoryEntity> listTree() {
        List<CategoryEntity> list = super.list();
//        取出顶层分类
        List<CategoryEntity> topLevelCategories = list.stream().filter(new Predicate<CategoryEntity>() {
            @Override
            public boolean test(CategoryEntity categoryEntity) {
                if (categoryEntity.getSort() == null) {
//                    设置sort字段
                    categoryEntity.setSort(0);
                }
                return categoryEntity.getParentCid() == 0;
            }
        }).sorted(new Comparator<CategoryEntity>() {
            @Override
            public int compare(CategoryEntity o1, CategoryEntity o2) {
                return o1.getSort().compareTo(o2.getSort());
            }
        }).collect(Collectors.toList());

//        获取子分类
        for (CategoryEntity root : topLevelCategories) {
            getChildren(root, list);
        }
        return topLevelCategories;
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        CategoryEntity categoryEntity = this.getById(catelogId);
        List<Long> path = new ArrayList<>();
        getCatelogPath(categoryEntity, path);
        return path.toArray(new Long[0]);
    }

    private void getCatelogPath(CategoryEntity categoryEntity, List<Long> path) {
        if (categoryEntity.getParentCid() != null && categoryEntity.getParentCid() != 0) {
            getCatelogPath(this.getById(categoryEntity.getParentCid()), path);
        }
        path.add(categoryEntity.getCatId());
    }

    private void getChildren(CategoryEntity root, List<CategoryEntity> list) {
        if (root.getChildren() == null) {
            root.setChildren(new ArrayList<>());
        }
        for (CategoryEntity entity : list) {
            if (entity.getParentCid().equals(root.getCatId())) {
                root.getChildren().add(entity);
                getChildren(entity, list);
            }
        }
        root.getChildren().sort(new Comparator<CategoryEntity>() {
            @Override
            public int compare(CategoryEntity o1, CategoryEntity o2) {
                return o1.getSort().compareTo(o2.getSort());
            }
        });
        if (root.getChildren().size() == 0) {
            root.setChildren(null);
        }
    }

}