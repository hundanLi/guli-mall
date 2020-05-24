package com.hundanli.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.to.SkuReductionTo;
import com.hundanli.common.to.SpuBoundsTo;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.common.utils.R;
import com.hundanli.gulimall.product.dao.SpuInfoDao;
import com.hundanli.gulimall.product.entity.*;
import com.hundanli.gulimall.product.feign.CouponFeignService;
import com.hundanli.gulimall.product.service.*;
import com.hundanli.common.utils.Utils;
import com.hundanli.gulimall.product.vo.spu.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author li
 */
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        //1、保存spu基本信息 pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setCreateTime(new Date());
        this.save(infoEntity);

        //2、保存Spu的描述图片url pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",", decript));
        spuInfoDescService.save(descEntity);

        //3、保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        List<SpuImagesEntity> imagesEntityList = images.stream().map(imgUrl -> {
            SpuImagesEntity imagesEntity = new SpuImagesEntity();
            imagesEntity.setSpuId(infoEntity.getId());
            imagesEntity.setImgUrl(imgUrl);
            return imagesEntity;
        }).collect(Collectors.toList());
        spuImagesService.saveBatch(imagesEntityList);

        //4、保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> attrValueEntityList = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity attrValue = new ProductAttrValueEntity();
            attrValue.setSpuId(infoEntity.getId());
            attrValue.setAttrId(attr.getAttrId());
            attrValue.setAttrValue(attr.getAttrValues());
            attrValue.setQuickShow(attr.getShowDesc());
            // 查询并设置属性名称
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            attrValue.setAttrName(attrEntity.getAttrName());

            return attrValue;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(attrValueEntityList);

        //5、保存spu的积分信息；gulimall_sms->sms_spu_bounds
        // TODO 保存spu的积分信息
        Bounds bounds = vo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(infoEntity.getId());
        R r1 = couponFeignService.save(spuBoundsTo);
        if (r1.getCode() != 0) {
            log.error("保存积分信息失败");
        }
        //6、保存当前spu对应的所有sku信息

        List<Skus> skusList = vo.getSkus();
        skusList.forEach(skus -> {
            // 6.1 sku的基本信息，pms_sku_info
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            // skuName, price, skuTitle, skuSubtitle;
            BeanUtils.copyProperties(skus, skuInfoEntity);
            skuInfoEntity.setBrandId(infoEntity.getBrandId());
            skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
            skuInfoEntity.setSpuId(infoEntity.getId());
            skuInfoEntity.setSaleCount(0L);
            List<Images> skusImages = skus.getImages();
            String defaultImg = "";
            for (Images image : skusImages) {
                if (image.getDefaultImg() == 1) {
                    defaultImg = image.getImgUrl();
                    break;
                }
            }
            skuInfoEntity.setSkuDefaultImg(defaultImg);
            skuInfoService.save(skuInfoEntity);

            Long skuId = skuInfoEntity.getSkuId();

            // 6.2 sku的图片信息，pms_sku_images
            List<SkuImagesEntity> skuImagesEntityList = skusImages.stream().filter(image -> {
                return !StringUtils.isEmpty(image.getImgUrl());
            }).map(image -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setImgUrl(image.getImgUrl());
                skuImagesEntity.setDefaultImg(image.getDefaultImg());
                return skuImagesEntity;
            }).collect(Collectors.toList());
            skuImagesService.saveBatch(skuImagesEntityList);

            // 6.3 sku的销售属性，pms_sku_sale_attr_value
            List<SkuSaleAttrValueEntity> saleAttrValueEntityList = skus.getAttr().stream().map(attr -> {
                SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, attrValueEntity);
                attrValueEntity.setSkuId(skuId);
                return attrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(saleAttrValueEntityList);

            // 6.4 sku的优惠满减信息，gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
            //TODO sku的优惠满减信息
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(skus, skuReductionTo);
            skuReductionTo.setSkuId(skuId);
            if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                R r2 = couponFeignService.saveSkuReduction(skuReductionTo);
                if (r2.getCode() != 0) {
                    log.error("保存优惠券信息失败");
                }
            }

        });

    }


    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        // 拼装查询条件
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        String status = (String) params.get("status");
        if (Utils.isNumeric(brandId)) {
            long l = Long.parseLong(brandId);
            if (l > 0) {
                queryWrapper.eq("brand_id", l);
            }
        }
        if (Utils.isNumeric(catelogId)) {
            long l = Long.parseLong(catelogId);
            if (l > 0) {
                queryWrapper.eq("catalog_id", l);
            }
        }
        if (Utils.isNumeric(status)) {
            queryWrapper.eq("publish_status", Long.parseLong(status));
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(wrapper -> {
                if (Utils.isNumeric(key)) {
                    wrapper.eq("id", Long.parseLong(key))
                            .or().like("spu_name", key);
                } else {
                    wrapper.like("spu_name", key);
                }
            });
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

}