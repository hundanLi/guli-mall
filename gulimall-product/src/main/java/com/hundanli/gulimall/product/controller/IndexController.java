package com.hundanli.gulimall.product.controller;

import com.hundanli.gulimall.product.entity.CategoryEntity;
import com.hundanli.gulimall.product.service.CategoryService;
import com.hundanli.gulimall.product.vo.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/10/31 13:52
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/", "/index.html"})
    public String indexPage(Model model) {
        List<CategoryEntity> level1Categories = categoryService.getLevel1Categories();
        model.addAttribute("categories", level1Categories);
        return "index";
    }

    @ResponseBody
    @GetMapping(value = "index/catalog.json")
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }


}
