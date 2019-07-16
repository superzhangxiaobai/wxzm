package com.wf.ew.light.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.LampTask;

public interface LampTaskService extends IService<LampTask> {

	PageResult<LampTask> listLampTask(Integer page, Integer limit, String searchKey, String searchValue);
	
	boolean add(LampTask lampTask);
	
	boolean update(LampTask lampTask);
	
	LampTask getByTaskType(Integer leixing);
}
