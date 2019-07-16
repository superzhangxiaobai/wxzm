package com.wf.ew.light.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wf.ew.light.model.Lamp;

public interface LampMapper extends BaseMapper<Lamp> {

	/**
	 * 根据路灯名查询路灯
	 * @param lampname
	 * @return
	 */
	Lamp selectByLampname (String lampname) ;
	
}
