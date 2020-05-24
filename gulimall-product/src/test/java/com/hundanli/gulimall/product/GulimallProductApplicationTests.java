package com.hundanli.gulimall.product;

import com.hundanli.gulimall.product.entity.BrandEntity;
import com.hundanli.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;


    @Test
    void testInsert() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("雷鸟科技");
        boolean save = brandService.save(brandEntity);
        assert save;
    }

    @Test
    void testUpdate() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("雷鸟科技");
        boolean update = brandService.updateById(brandEntity);
        assert update;
    }

    @Test
    void testSelectList() {
        List<BrandEntity> list = brandService.list();
        list.forEach(System.out::println);
    }



}
