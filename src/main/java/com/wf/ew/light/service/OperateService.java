package com.wf.ew.light.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Operate;

public interface OperateService extends IService<Operate> {

	PageResult<Operate> listOperate(Integer page, Integer limit, String searchKey, String searchValue);
	
	boolean add (Operate operate) ;
	
	boolean update (Operate operate) ;
	
}
