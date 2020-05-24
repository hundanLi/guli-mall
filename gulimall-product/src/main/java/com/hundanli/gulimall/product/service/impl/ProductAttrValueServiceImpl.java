package com.hundanli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.gulimall.product.dao.ProductAttrValueDao;
import com.hundanli.gulimall.product.entity.ProductAttrValueEntity;
import com.hundanli.gulimall.product.service.ProductAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> spuAttrValues(Long spuId) {
        QueryWrapper<ProductAttrValueEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id", spuId);
        return this.list(wrapper);
    }

    @Override
    public void updateSpuAttrValues(Long spuId, List<ProductAttrValueEntity> attrValueEntityList) {
        // 删除旧属性值
        this.remove(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        attrValueEntityList.forEach(item -> {
            item.setSpuId(spuId);
        });
        this.saveBatch(attrValueEntityList);
    }

}