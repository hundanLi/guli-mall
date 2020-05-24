package com.hundanli.gulimall.product.controller;

import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.R;
import com.hundanli.gulimall.product.entity.AttrEntity;
import com.hundanli.gulimall.product.entity.AttrGroupEntity;
import com.hundanli.gulimall.product.service.AttrAttrgroupRelationService;
import com.hundanli.gulimall.product.service.AttrGroupService;
import com.hundanli.gulimall.product.service.CategoryService;
import com.hundanli.gulimall.product.vo.AttrGroupRelationVo;
import com.hundanli.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;



    /**
     * 查询指定分类的所有属性分组列表
     */
    @RequestMapping("/list/{catelogId}")

    //@RequiresPermissions("product:attrgroup:list")
    public R relationAttrs(@RequestParam Map<String, Object> params, @PathVariable Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 获取关联了指定分组的所有属性
     *
     * @param attrGroupId 属性分组id
     * @return R
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R relationAttrs(@PathVariable("attrGroupId") Long attrGroupId) {

        List<AttrEntity> attrs = attrGroupService.getAttrsByGroupId(attrGroupId);

        return R.ok().put("data", attrs);
    }

    /** 获取没有与指定分组关联的属性列表
     * @param attrGroupId 分组id
     * @return R
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R noRelationAttrs(@RequestParam Map<String, Object> params, @PathVariable Long attrGroupId) {
        PageUtils pageUtils = attrGroupService.getNoRelationAttrsByGroupId(params, attrGroupId);

        return R.ok().put("data", pageUtils);
    }

    /** 新增关联属性
     * @param relationVos vos
     * @return R
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody AttrGroupRelationVo[] relationVos) {
        attrAttrgroupRelationService.addRelations(relationVos);
        return R.ok();
    }

    /** 批量删除关联关系
     * @param relationVos vos
     * @return R
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] relationVos) {
        attrAttrgroupRelationService.deleteRelations(relationVos);
        return R.ok();
    }

    @GetMapping("/{catelogId}/withattr")
    public R groupWithAttrsByCategory(@PathVariable Long catelogId) {
        List<AttrGroupWithAttrsVo> attrsVos = attrGroupService.getGroupWithAttrsByCatId(catelogId);
        return R.ok().put("data", attrsVos);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {

        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        // 找出catelogId的完整路径
        Long[] catelogPath = categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
