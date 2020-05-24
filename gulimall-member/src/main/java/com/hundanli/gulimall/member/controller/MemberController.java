package com.hundanli.gulimall.member.controller;

import com.hundanli.common.utils.PageUtils;
import com.hundanli.common.utils.R;
import com.hundanli.gulimall.member.entity.MemberEntity;
import com.hundanli.gulimall.member.feign.CouponFeignService;
import com.hundanli.gulimall.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 会员
 *
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 14:12:46
 * 注解@RefreshScope用于动态获取配置中心的配置
 */
@RefreshScope
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private CouponFeignService couponFeignService;

    @Value("${member.name}")
    String name;
    @Value("${member.age}")
    Integer age;

    @GetMapping("/config")
    public R remoteConfig() {
        return R.ok().put("member.name", name).put("member.age", age);
    }

    /** openfeign 示例
     * @return R
     */
    @GetMapping("/coupons")
    public R coupons() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("hundanli");
        R r = couponFeignService.memberCoupons();
        return R.ok().put("member", memberEntity).put("coupons", r.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
