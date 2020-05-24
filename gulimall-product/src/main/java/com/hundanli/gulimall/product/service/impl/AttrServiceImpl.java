package com.hundanli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.constant.ProductConstant;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.gulimall.product.dao.AttrDao;
import com.hundanli.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.hundanli.gulimall.product.entity.AttrEntity;
import com.hundanli.gulimall.product.entity.AttrGroupEntity;
import com.hundanli.gulimall.product.entity.CategoryEntity;
import com.hundanli.gulimall.product.service.AttrAttrgroupRelationService;
import com.hundanli.gulimall.product.service.AttrGroupService;
import com.hundanli.gulimall.product.service.AttrService;
import com.hundanli.gulimall.product.service.CategoryService;
import com.hundanli.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, String attrType, Long catId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        if (ProductConstant.AttrConstant.BASE.getMsg().equals(attrType)) {
            // 基本属性
            queryWrapper.eq("attr_type", ProductConstant.AttrConstant.BASE.getCode());
        } else if (ProductConstant.AttrConstant.SALE.getMsg().equals(attrType)) {
            // 销售属性
            queryWrapper.eq("attr_type", ProductConstant.AttrConstant.SALE.getCode());
        }
        if (catId != 0) {
            queryWrapper.eq("catelog_id", catId);
            String key = (String) params.get("key");
            if (!StringUtils.isEmpty(key)) {
                queryWrapper.like("attr_name", key);
            }
        }

        IPage<AttrEntity> iPage = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        List<AttrVo> attrVoList = iPage.getRecords().stream().map(attrEntity -> {
            // VO对象
            AttrVo attrVo = new AttrVo();
            BeanUtils.copyProperties(attrEntity, attrVo);

            // 设置分类名称和组名称
            if (attrVo.getCatelogId() != null && attrVo.getCatelogId() > 0) {
                CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
                attrVo.setCatelogName(categoryEntity.getName());
            }

            if (ProductConstant.AttrConstant.BASE.getMsg().equals(attrType)) {
                AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService
                        .getOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                                .eq("attr_id", attrEntity.getAttrId()));
                if (relationEntity != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                    attrVo.setGroupName(attrGroupEntity.getAttrGroupName());
                    attrVo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
                }
            }

            return attrVo;
        }).collect(Collectors.toList());


        PageUtils page = new PageUtils(iPage);
        page.setList(attrVoList);
        return page;
    }

    @Override
    public void save(AttrVo attrVo) {
        // 保存属性，回送id
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.save(attrEntity);

        // 保存属性和分组的关联信息
        if (attrVo.getAttrType() == ProductConstant.AttrConstant.BASE.getCode() && attrVo.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationService.save(relationEntity);
        }
    }

    @Override
    public void updateById(AttrVo attrVo) {
        // 保存属性
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.updateById(attrEntity);

        if (attrVo.getAttrType() == ProductConstant.AttrConstant.BASE.getCode()) {
            // 更新分组关联信息
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            // 存在则更新，否则插入
            if (relationEntity == null) {
                relationEntity = new AttrAttrgroupRelationEntity();
                relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
                relationEntity.setAttrId(attrVo.getAttrId());
                attrAttrgroupRelationService.save(relationEntity);
            } else {
                relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
                attrAttrgroupRelationService.updateById(relationEntity);
            }
        }

    }

    @Override
    public AttrVo getAttr(Long attrId) {
        // 获取基本属性信息
        AttrEntity attrEntity = this.getById(attrId);
        if (attrEntity == null) {
            return null;
        }
        AttrVo attrVo = new AttrVo();
        BeanUtils.copyProperties(attrEntity, attrVo);

        // 设置分组名称和分类名称
        if (attrVo.getAttrType() == ProductConstant.AttrConstant.BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (relationEntity != null) {
                AttrGroupEntity attrGroupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                attrVo.setGroupName(attrGroupEntity.getAttrGroupName());
                attrVo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
            }
        }

        if (attrVo.getCatelogId() != null && attrVo.getCatelogId() > 0) {
            CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            attrVo.setCatelogName(categoryEntity.getName());
            // 设置三级分类路径
            Long[] catelogPath = categoryService.findCatelogPath(categoryEntity.getCatId());
            attrVo.setCatelogPath(catelogPath);
        }

        return attrVo;
    }
}