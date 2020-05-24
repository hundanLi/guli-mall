package com.hundanli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.hundanli.gulimall.product.vo.AttrGroupRelationVo;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void deleteRelations(AttrGroupRelationVo[] relationVos);

    void addRelations(AttrGroupRelationVo[] relationVos);
}

