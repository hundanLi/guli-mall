package com.hundanli.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.ware.entity.PurchaseEntity;
import com.hundanli.gulimall.ware.vo.MergeVo;
import com.hundanli.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 14:46:02
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchaseDetail(MergeVo mergeVo);

    void receivePurchase(List<Long> ids);

    void donePurchase(PurchaseDoneVo vo);
}

