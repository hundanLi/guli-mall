package com.hundanli.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hundanli.common.constant.WareConstant;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.Query;
import com.hundanli.gulimall.ware.dao.PurchaseDao;
import com.hundanli.gulimall.ware.entity.PurchaseDetailEntity;
import com.hundanli.gulimall.ware.entity.PurchaseEntity;
import com.hundanli.gulimall.ware.service.PurchaseDetailService;
import com.hundanli.gulimall.ware.service.PurchaseService;
import com.hundanli.gulimall.ware.service.WareSkuService;
import com.hundanli.gulimall.ware.vo.MergeVo;
import com.hundanli.gulimall.ware.vo.PurchaseDoneVo;
import com.hundanli.gulimall.ware.vo.PurchaseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergePurchaseDetail(MergeVo mergeVo) {
        // 如果不指定采购单则新建
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseConstant.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            // 确认采购单的状态为0或者1
            PurchaseEntity old = this.getById(purchaseId);
            if (old.getStatus() >= WareConstant.PurchaseConstant.RECEIVED.getCode()) {
                return;
            }
            // 保存更新时间
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setId(purchaseId);
            purchaseEntity.setUpdateTime(new Date());
            this.updateById(purchaseEntity);
        }

        // 合并采购需求
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map(i -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailConstant.ASSIGNED.getCode());
            purchaseDetailEntity.setId(i);
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailEntityList);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receivePurchase(List<Long> ids) {
        // 1.确认采购单是已分配或者新建状态
        List<PurchaseEntity> purchaseEntities = this.listByIds(ids).stream().filter(purchaseEntity -> {
            return purchaseEntity.getStatus() < WareConstant.PurchaseConstant.RECEIVED.getCode();
        }).peek(purchaseEntity -> {
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseConstant.RECEIVED.getCode());
        }).collect(Collectors.toList());
        if (purchaseEntities.size() <= 0) {
            return;
        }
        // 2.更改采购单状态
        this.updateBatchById(purchaseEntities);

        // 3.更改采购需求的状态
        List<Long> purchaseIds = purchaseEntities.stream().map(PurchaseEntity::getId).collect(Collectors.toList());
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.in("purchase_id", purchaseIds);
        List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.list(wrapper);
        List<PurchaseDetailEntity> detailEntities = purchaseDetailEntityList.stream().map(item -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item.getId());
            detailEntity.setStatus(WareConstant.PurchaseDetailConstant.BUYING.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(detailEntities);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void donePurchase(PurchaseDoneVo vo) {
        // 更新采购需求的状态
        boolean success = true;
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        for (PurchaseItem item : vo.getItems()) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item.getItemId());
            detailEntity.setStatus(item.getStatus());
            if (detailEntity.getStatus() != WareConstant.PurchaseDetailConstant.FINISHED.getCode()) {
                success = false;
            }
            updates.add(detailEntity);
        }
        purchaseDetailService.updateBatchById(updates);

        // 更新采购单的状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(vo.getId());
        purchaseEntity.setUpdateTime(new Date());
        if (success) {
            purchaseEntity.setStatus(WareConstant.PurchaseConstant.FINISHED.getCode());
        } else {
            purchaseEntity.setStatus(WareConstant.PurchaseConstant.ERROR.getCode());
        }
        this.updateById(purchaseEntity);

        //更新库存
        List<Long> purchaseDetailIds = vo.getItems().stream().filter(item->{
            return item.getStatus() == WareConstant.PurchaseDetailConstant.FINISHED.getCode();
        }).map(PurchaseItem::getItemId).collect(Collectors.toList());
        List<PurchaseDetailEntity> detailEntityList = purchaseDetailService.listByIds(purchaseDetailIds);
        wareSkuService.addStocks(detailEntityList);

    }

}