package com.hundanli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.product.entity.AttrEntity;
import com.hundanli.gulimall.product.entity.AttrGroupEntity;
import com.hundanli.gulimall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, long catelogId);

    List<AttrEntity> getAttrsByGroupId(Long attrGroupId);

    PageUtils getNoRelationAttrsByGroupId(Map<String, Object> params, Long attrGroupId);

    List<AttrGroupWithAttrsVo> getGroupWithAttrsByCatId(Long catelogId);
}

