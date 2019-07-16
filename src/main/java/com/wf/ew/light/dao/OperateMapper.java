package com.wf.ew.light.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wf.ew.light.model.Operate;

public interface OperateMapper extends BaseMapper<Operate> {

	/**
	 * 查询最新4条操作，根用户表关联查询
	 * @return
	 */
	List<Operate> selectByNickName();
	
}
