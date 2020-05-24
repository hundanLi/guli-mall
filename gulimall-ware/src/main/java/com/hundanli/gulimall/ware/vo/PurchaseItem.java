package com.hundanli.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-23 11:15
 **/
@Data
public class PurchaseItem {
    @NotNull
    private Long itemId;
    @NotNull
    private Integer status;
    private String reason;
}
