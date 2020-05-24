package com.hundanli.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-22 10:48
 **/
@Data
public class SpuBoundsTo {
    private Long spuId;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;
    /**
     * 购物积分
     */
    private BigDecimal buyBounds;
}
