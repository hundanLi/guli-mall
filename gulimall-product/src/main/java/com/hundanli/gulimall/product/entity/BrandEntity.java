package com.hundanli.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hundanli.common.valid.AddGroup;
import com.hundanli.common.valid.SetValue;
import com.hundanli.common.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 12:42:21
 * 1.分组校验：对不同请求，校验规则各异
 *  1.1 新建XxGroup空接口作为区分
 *  1.2 字段注解添加groups选项
 *  1.3 Controller方法上使用@Validated(groups={})注解，不指定group则不校验
 *
 * 2.自定义校验器
 *  2.1编写校验注解类
 *  2.2编写校验器类
 *  2.3关联注解类和校验器类
 *
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @NotNull(groups = UpdateGroup.class)
    @Null(groups = AddGroup.class)
    @TableId
    private Long brandId;

    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空", groups = {AddGroup.class})
    private String name;

    /**
     * 品牌logo地址
     */
    @NotBlank(message = "品牌logo地址不能为空", groups = AddGroup.class)
    @URL(message = "品牌logo地址必须是合法的url", groups = {AddGroup.class, UpdateGroup.class})
    private String logo;

    /**
     * 介绍
     */
    private String descript;

    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(groups = AddGroup.class, message = "显示状态不能为空")
    @SetValue(values = {0, 1}, groups = {AddGroup.class, UpdateGroup.class})
    private Integer showStatus;

    /**
     * 检索首字母
     */
    @NotBlank(message = "检索首字母不能为空", groups = AddGroup.class)
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是a-z或者A-Z", groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = AddGroup.class)
    @Min(value = 0, message = "排序必须大于等于0", groups = {AddGroup.class, UpdateGroup.class})
    private Integer sort;

}
