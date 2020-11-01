package com.hundanli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.gulimall.product.dao.CategoryDao;
import com.hundanli.gulimall.product.entity.CategoryEntity;
import com.hundanli.gulimall.product.service.CategoryService;
import com.hundanli.gulimall.product.vo.Catalog2Vo;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
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

    @Override
    public List<CategoryEntity> getLevel1Categories() {
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEntity::getCatLevel, 1);
        return this.list(queryWrapper);
    }

    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        // 1.找出一级分类
        List<CategoryEntity> level1List = getLevel1Categories();
        if (level1List == null) {
            return new HashMap<>(0);
        }
        // 2.找出每个一级分类的二级分类列表
        Map<String, List<Catalog2Vo>> map = level1List.stream().collect(Collectors.toMap(
                level1Entity -> level1Entity.getCatId().toString(),
                level1Entity -> {
                    LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(CategoryEntity::getParentCid, level1Entity.getCatId());
                    List<CategoryEntity> level2List = baseMapper.selectList(queryWrapper);
                    if (level2List == null) {
                        return Collections.emptyList();
                    }
                    return level2List.stream().map(level2Entity -> {
                        // 构造二级分类VO
                        Catalog2Vo catalog2Vo = new Catalog2Vo();
                        catalog2Vo.setCatalog1Id(level1Entity.getCatId().toString());
                        catalog2Vo.setId(level2Entity.getCatId().toString());
                        catalog2Vo.setName(level2Entity.getName());
                        catalog2Vo.setCatalog3List(Collections.emptyList());
                        // 为每个二级分类查出三级子分类列表
                        LambdaQueryWrapper<CategoryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(CategoryEntity::getParentCid, level2Entity.getCatId());
                        List<CategoryEntity> level3List = baseMapper.selectList(lambdaQueryWrapper);
                        if (level3List == null) {
                            return catalog2Vo;
                        }
                        List<Catalog2Vo.Catalog3Vo> catalog3VoList = level3List.stream().map(level3Entity -> {
                            Catalog2Vo.Catalog3Vo catalog3Vo = new Catalog2Vo.Catalog3Vo();
                            catalog3Vo.setCatalog2Id(level2Entity.getCatId().toString());
                            catalog3Vo.setId(level3Entity.getCatId().toString());
                            catalog3Vo.setName(level3Entity.getName());
                            return catalog3Vo;
                        }).collect(Collectors.toList());
                        catalog2Vo.setCatalog3List(catalog3VoList);

                        return catalog2Vo;
                    }).collect(Collectors.toList());
                }
        ));
        // 返回结果
        return map;
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