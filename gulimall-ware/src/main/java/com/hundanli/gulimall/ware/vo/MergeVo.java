package com.hundanli.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-23 09:26
 **/
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
