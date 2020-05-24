package com.hundanli.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 13:56:10
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

