package com.hundanli.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-22 11:10
 **/
@Data
public class SkuReductionTo {
    private Long skuId;
    private Integer fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
