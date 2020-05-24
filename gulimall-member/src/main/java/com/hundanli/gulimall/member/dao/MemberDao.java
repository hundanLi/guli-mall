package com.hundanli.gulimall.member.dao;

import com.hundanli.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author hundanli
 * @email hundanlee@gmail.com
 * @date 2020-05-10 14:12:46
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
