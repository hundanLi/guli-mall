package com.hundanli.gulimall.product.controller;

import com.hundanli.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/10/31 13:52
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/", "/index.html"})
    public String indexPage(Model model) {

        return "index";
    }
}
