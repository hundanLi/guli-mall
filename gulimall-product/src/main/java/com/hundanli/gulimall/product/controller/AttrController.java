package com.hundanli.gulimall.product.controller;

import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.R;
import com.hundanli.gulimall.product.entity.ProductAttrValueEntity;
import com.hundanli.gulimall.product.service.AttrService;
import com.hundanli.gulimall.product.service.ProductAttrValueService;
import com.hundanli.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;


    @PostMapping("/update/{spuId}")
    public R updateSpuAttrValue(@PathVariable Long spuId, @RequestBody List<ProductAttrValueEntity> attrValueEntityList) {
        attrValueService.updateSpuAttrValues(spuId, attrValueEntityList);
        return R.ok();
    }

    @GetMapping("/base/listforspu/{spuId}")
    public R attrValueForSpu(@PathVariable Long spuId) {
        List<ProductAttrValueEntity> data = attrValueService.spuAttrValues(spuId);
        return R.ok().put("data", data);
    }

    /**
     * 获取基本属性列表
     */
    @RequestMapping("/{attrType}/list/{catId}")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable String attrType, @PathVariable Long catId) {
        PageUtils page = attrService.queryPage(params, attrType, catId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrVo attrVo = attrService.getAttr(attrId);

        return R.ok().put("attr", attrVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attrVo) {
        attrService.save(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attrVo) {
        attrService.updateById(attrVo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
