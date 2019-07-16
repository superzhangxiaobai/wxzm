package com.wf.ew.light.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.light.dao.LampTaskMapper;
import com.wf.ew.light.model.LampTask;
import com.wf.ew.light.service.LampTaskService;

@Service
public class LampTaskServiceImpl extends ServiceImpl<LampTaskMapper, LampTask> implements LampTaskService {

	@Override
	public PageResult<LampTask> listLampTask(Integer page, Integer limit, String searchKey, String searchValue) {
		if (page == null || limit == null) {
            page = 1;
            limit = 10;
        }
		QueryWrapper<LampTask> wrapper = new QueryWrapper<>();
		if (StringUtil.isNotBlank(searchValue) && StringUtil.isNotBlank(searchKey)) {
            wrapper.eq(searchKey, searchValue);
        }
		IPage<LampTask> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
		return new PageResult<>(iPage.getRecords(), iPage.getTotal());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean add(LampTask lampTask) {
		boolean rs = baseMapper.insert(lampTask) > 0;
		return rs;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(LampTask lampTask) {
		boolean rs = baseMapper.updateById(lampTask) > 0;
		return rs;
	}

	@Override
	public LampTask getByTaskType(Integer leixing) {
		
		return baseMapper.selectByTaskType(leixing);
	}

	

}
