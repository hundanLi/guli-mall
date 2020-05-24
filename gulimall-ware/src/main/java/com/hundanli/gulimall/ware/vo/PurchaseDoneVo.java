package com.hundanli.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-23 11:14
 **/
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;
    private List<PurchaseItem> items;
}
