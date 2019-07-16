package com.wf.ew.light.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.light.dao.OperateMapper;
import com.wf.ew.light.model.Operate;
import com.wf.ew.light.service.OperateService;

@Service
public class OperateServiceImpl extends ServiceImpl<OperateMapper, Operate> implements OperateService {

	@Override
	public PageResult<Operate> listOperate(Integer page, Integer limit, String searchKey, String searchValue) {
		if (page == null || limit == null) {
            page = 1;
            limit = 10;
        }
		QueryWrapper<Operate> wrapper = new QueryWrapper<>();
		wrapper.orderByDesc("update_time");
		if (StringUtil.isNotBlank(searchValue) && StringUtil.isNotBlank(searchKey)) {
			System.out.println("=============="+searchValue);
            //wrapper.eq(searchKey, searchValue);
            if (searchValue.equals("curdate()")) {
				wrapper.apply("date(update_time) = curdate()", null);
			}
            if (searchValue.equals("month")) {
            	wrapper.apply("ORDER BY update_time DESC", null);
			}
            if (searchKey.equals("lamp_id")) {
            	wrapper.eq(searchKey, searchValue);
			}
            if (searchKey.equals("weekly")) {
            	wrapper.apply("date(update_time) = "+searchValue+"", null);
			}
        }
		
		//IPage<Operate> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
		List<Operate> selectByNickName = baseMapper.selectByNickName();
		return new PageResult<>(selectByNickName, selectByNickName.size());
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean add(Operate operate) {
		boolean rs = baseMapper.insert(operate) > 0;
		return rs;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(Operate operate) {
		boolean rs = baseMapper.updateById(operate) > 0;
		return rs;
	}

}
