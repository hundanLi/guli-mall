package com.hundanli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hundanli.common.utils.PageUtils;
import com.hundanli.gulimall.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

