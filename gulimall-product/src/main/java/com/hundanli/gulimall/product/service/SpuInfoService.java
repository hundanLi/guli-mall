package com.hundanli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.product.entity.SpuInfoEntity;
import com.hundanli.gulimall.product.vo.spu.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

