package com.hundanli.gulimall.product.feign;

import com.hundanli.common.to.SkuReductionTo;
import com.hundanli.common.to.SpuBoundsTo;
import com.hundanli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-22 10:37
 **/
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * 远程调用逻辑：
     *  1.@RequestBody将对象转为json，调用对应的远程方法并将json数据进行传输
     *  2.远程方法根据@RequestBody注解将json转为相应的对象（不需要与调用方一致，只需字段名统一即可）
     *  3.远程方法执行完成并返回
     *
     * @param spuBoundsTo 传输对象
     * @return R
     */
    @PostMapping("/coupon/spubounds/save")
    //@RequiresPermissions("coupon:spubounds:save")
    R save(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(SkuReductionTo skuReductionTo);
}
