package com.hundanli.gulimall.product.controller;

import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.R;
import com.hundanli.common.valid.AddGroup;
import com.hundanli.common.valid.UpdateGroup;
import com.hundanli.gulimall.product.entity.BrandEntity;
import com.hundanli.gulimall.product.service.BrandService;
import com.hundanli.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 品牌
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(value = AddGroup.class) @RequestBody BrandEntity brand /*BindingResult result*/) {
//        if (result.hasFieldErrors()) {
//            Map<String, Object> map = new HashMap<>(result.getFieldErrors().size());
//            result.getFieldErrors().forEach(new Consumer<FieldError>() {
//                @Override
//                public void accept(FieldError fieldError) {
//                    map.put(fieldError.getField(), fieldError.getDefaultMessage());
//                }
//            });
//            return R.error(400, "提交的数据不合法!").put("data", map);
//        }

        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(value = UpdateGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);
        if (!StringUtils.isEmpty(brand.getName())) {
            // 级联更新品牌-分类关系表
            categoryBrandRelationService.updateBrandName(brand);
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
