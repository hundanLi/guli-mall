package com.hundanli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.constant.ProductConstant;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.gulimall.product.dao.AttrGroupDao;
import com.hundanli.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.hundanli.gulimall.product.entity.AttrEntity;
import com.hundanli.gulimall.product.entity.AttrGroupEntity;
import com.hundanli.gulimall.product.service.AttrAttrgroupRelationService;
import com.hundanli.gulimall.product.service.AttrGroupService;
import com.hundanli.gulimall.product.service.AttrService;
import com.hundanli.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, long catelogId) {
        // select * from pms_attr_group where catelog_id = ? and (attr_group_id = ? or attr_group_name like ?)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(queryWrapper -> {
                queryWrapper.eq("attr_group_id", params.get("key")).or().like("attr_group_name", params.get("key"));
            });
        }
        if (catelogId > 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getAttrsByGroupId(Long attrGroupId) {

        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id", attrGroupId);
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationService.list(queryWrapper);
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<>();
        List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        attrEntityQueryWrapper.in("attr_id", attrIds);

        List<AttrEntity> attrs = null;
        if (attrIds.size() > 0) {
            attrs = attrService.list(attrEntityQueryWrapper);
        }

        return attrs;
    }

    @Override
    public PageUtils getNoRelationAttrsByGroupId(Map<String, Object> params ,Long attrGroupId) {
        // 1.找出该组所属的分类
        AttrGroupEntity groupEntity = this.getById(attrGroupId);
        if (groupEntity == null) {
            return null;
        }

        // 2.找出本分类下所有组和关联关系
        // 找出分类下所有组
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id", groupEntity.getCatelogId());
        List<AttrGroupEntity> groupEntityList = this.list(queryWrapper);

        // 找出关联关系
        List<Long> groupIds = groupEntityList.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.in("attr_group_id", groupIds);
        List<AttrAttrgroupRelationEntity> relationEntityList = attrAttrgroupRelationService.list(wrapper);
        Set<Long> relatedAttrIds = relationEntityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toSet());

        // 3.排除本分类下已被关联的这些属性
        QueryWrapper<AttrEntity> attrQueryWrapper = new QueryWrapper<>();
        attrQueryWrapper.eq("catelog_id", groupEntity.getCatelogId()).eq("attr_type", ProductConstant.AttrConstant.BASE.getCode());
        if (relatedAttrIds.size() > 0) {
            attrQueryWrapper.notIn("attr_id", relatedAttrIds);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            attrQueryWrapper.like("attr_name", key);
        }
        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), attrQueryWrapper);

        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getGroupWithAttrsByCatId(Long catelogId) {
        // 查出属性分组
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id", catelogId);
        List<AttrGroupEntity> groupEntityList = this.list(queryWrapper);
        // 查出属性
        return groupEntityList.stream().map(groupEntity -> {
            AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(groupEntity, vo);
            List<AttrEntity> attrs = this.getAttrsByGroupId(vo.getAttrGroupId());
            vo.setAttrs(attrs);
            return vo;
        }).collect(Collectors.toList());
    }

}