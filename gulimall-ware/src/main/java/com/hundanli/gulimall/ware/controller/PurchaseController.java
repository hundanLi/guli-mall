package com.hundanli.gulimall.ware.controller;

import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.R;
import com.hundanli.gulimall.ware.entity.PurchaseEntity;
import com.hundanli.gulimall.ware.service.PurchaseService;
import com.hundanli.gulimall.ware.vo.MergeVo;
import com.hundanli.gulimall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 采购信息
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 14:46:02
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    /** 获取未领取的采购单
     * @param params 查询参数
     * @return  R
     */
    @GetMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }


    @PostMapping("/merge")
    public R mergePurchaseDetail(@RequestBody MergeVo mergeVo) {
        purchaseService.mergePurchaseDetail(mergeVo);
        return R.ok();
    }

    /**
     * 领取采购单
     *
     * @return R
     */
    @PostMapping("/receive")
    public R receivePurchase(@RequestBody List<Long> ids) {
        purchaseService.receivePurchase(ids);
        return R.ok();
    }

    @PostMapping("/done")
    public R finishPurchase(@RequestBody PurchaseDoneVo vo) {
        purchaseService.donePurchase(vo);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
