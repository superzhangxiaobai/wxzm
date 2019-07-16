package com.wf.ew.mv.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.mv.dao.RtspMapper;
import com.wf.ew.mv.model.Rtsp;
import com.wf.ew.mv.service.RtspService;

/**
 * 摄像头视频表 服务实现类
 *
 * @author null
 * @since 2019-03-21
 */
@Service
public class RtspServiceImpl extends ServiceImpl<RtspMapper, Rtsp> implements RtspService {

	@Override
	public PageResult<Rtsp> listRtsp(Integer page, Integer limit, String searchKey, String searchValue) {
		if (page == null || limit == null) {
            page = 1;
            limit = 10;
        }
		QueryWrapper<Rtsp> wrapper = new QueryWrapper<>();
		if (StringUtil.isNotBlank(searchValue) && StringUtil.isNotBlank(searchKey)) {
            wrapper.eq(searchKey, searchValue);
        }
		IPage<Rtsp> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
		
		return new PageResult<>(iPage.getRecords(), iPage.getTotal());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean add(Rtsp rtsp) {
		boolean rs = baseMapper.insert(rtsp) > 0;
		return rs;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(Rtsp rtsp) {
		 boolean rs = baseMapper.updateById(rtsp) > 0;
		return rs;
	}

}
