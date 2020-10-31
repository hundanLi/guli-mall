package com.hundanli.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/10/31 13:46
 *
 * 二级分类VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {

    /**
     * 父分类，即一级分类id
     */
    private String catelog1Id;
    private String id;
    private String name;
    private List<Catelog3Vo> catelog3List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo {

        /**
         * 父分类，即二级分类id
         */
        private String catelog2Id;
        private String id;
        private String name;
    }

}
