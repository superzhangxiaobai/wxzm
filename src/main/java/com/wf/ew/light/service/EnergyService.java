package com.wf.ew.light.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Energy;

public interface EnergyService extends IService<Energy> {

	PageResult<Energy> listEnergy(Integer page, Integer limit, String searchKey, String searchValue);
	
	PageResult<Energy> list(Integer page, Integer limit, String startDate, String endDate, String lampname);
	
	PageResult<Energy> report(String startDate, String endDate, String lampname);
	
	boolean add (Energy energy) ;
	
	boolean update (Energy energy) ;
	
}
