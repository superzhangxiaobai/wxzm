package com.wf.ew.light.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wf.ew.light.model.LampTask;

public interface LampTaskMapper extends BaseMapper<LampTask> {

	/**
	 * 根据类型查询定时任务
	 * @param leixing
	 * @return
	 */
	LampTask selectByTaskType(Integer leixing);
	
}
