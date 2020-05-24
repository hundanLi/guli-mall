package com.hundanli.gulimall.member.feign;

import com.hundanli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-10 16:18
 * FeignClient中的值是注册到nacos中的应用名称
 **/
@FeignClient(value = "gulimall-coupon")
public interface CouponFeignService {
    /**
     * 测试openfeign远程服务调用
     * 方法就是远程方法的签名
     * @return R
     */
    @GetMapping("/coupon/coupon/member/list")
    public R memberCoupons();
}
