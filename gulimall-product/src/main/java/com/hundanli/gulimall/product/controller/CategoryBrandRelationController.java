package com.hundanli.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.R;
import com.hundanli.gulimall.product.entity.BrandEntity;
import com.hundanli.gulimall.product.entity.CategoryBrandRelationEntity;
import com.hundanli.gulimall.product.service.CategoryBrandRelationService;
import com.hundanli.gulimall.product.vo.BrandVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 品牌分类关联
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 根据品牌ID获取关联的分类列表
     *
     * @return R
     */
    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam("brandId") Long brandId) {
        QueryWrapper<CategoryBrandRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id", brandId);
        List<CategoryBrandRelationEntity> brandRelationEntities = categoryBrandRelationService.list(queryWrapper);
        return R.ok().put("data", brandRelationEntities);
    }


    /** 根据分类获取关联的品牌列表
     * @param catId 分类id
     * @return R
     */
    @GetMapping("brands/list")
    public R brandList(@RequestParam(value = "catId") Long catId) {
        List<BrandEntity> brands = categoryBrandRelationService.getBrandList(catId);
        List<BrandVo> brandVos = brands.stream().map(brandEntity -> {
            BrandVo brandVo = new BrandVo();
            BeanUtils.copyProperties(brandEntity, brandVo);
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data", brandVos);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveRelation(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
