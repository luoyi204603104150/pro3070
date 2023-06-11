package com.DreamBBS.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.DreamBBS.entity.enums.CommentStatusEnum;
import com.DreamBBS.entity.po.ForumArticle;
import com.DreamBBS.mappers.ForumArticleMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.DreamBBS.entity.enums.PageSize;
import com.DreamBBS.entity.query.ForumCommentQuery;
import com.DreamBBS.entity.po.ForumComment;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.entity.query.SimplePage;
import com.DreamBBS.mappers.ForumCommentMapper;
import com.DreamBBS.service.ForumCommentService;
import com.DreamBBS.utils.StringTools;


/**
 * 评论 业务接口实现
 */
@Service("forumCommentService")
public class ForumCommentServiceImpl implements ForumCommentService {

	@Resource
	private ForumCommentMapper<ForumComment, ForumCommentQuery> forumCommentMapper;

	@Resource
	private ForumArticleMapper<ForumArticle, ForumCommentQuery> forumArticleMapper;

	@Resource
	@Lazy
	private ForumCommentServiceImpl forumCommentService;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<ForumComment> findListByParam(ForumCommentQuery param) {
		List<ForumComment> list = this.forumCommentMapper.selectList(param);
		//获取二级评论
		if (param.getLoadChildren() != null && param.getLoadChildren()) {
			ForumCommentQuery subQuery = new ForumCommentQuery();
			subQuery.setQueryLikeType(param.getQueryLikeType());
			subQuery.setCurrentUserId(param.getCurrentUserId());
			subQuery.setArticleId(param.getArticleId());
			subQuery.setLoadChildren(param.getLoadChildren());
			subQuery.setStatus(CommentStatusEnum.NORMAL.getStatus());
			subQuery.setOnlyQueryChildren(true);
			List<ForumComment> subCommentList = this.forumCommentMapper.selectList(subQuery);
			Map<Integer, List<ForumComment>> tempMap = subCommentList.stream().collect(Collectors.groupingBy(ForumComment::getpCommentId));
			list.forEach(item -> {
				item.setChildren(tempMap.get(item.getCommentId()));
			});
		}

		return list;
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(ForumCommentQuery param) {
		return this.forumCommentMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<ForumComment> findListByPage(ForumCommentQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<ForumComment> list = this.findListByParam(param);
		PaginationResultVO<ForumComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(ForumComment bean) {
		return this.forumCommentMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<ForumComment> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumCommentMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<ForumComment> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumCommentMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(ForumComment bean, ForumCommentQuery param) {
		StringTools.checkParam(param);
		return this.forumCommentMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(ForumCommentQuery param) {
		StringTools.checkParam(param);
		return this.forumCommentMapper.deleteByParam(param);
	}

	/**
	 * 根据CommentId获取对象
	 */
	@Override
	public ForumComment getForumCommentByCommentId(Integer commentId) {
		return this.forumCommentMapper.selectByCommentId(commentId);
	}

	/**
	 * 根据CommentId修改
	 */
	@Override
	public Integer updateForumCommentByCommentId(ForumComment bean, Integer commentId) {
		return this.forumCommentMapper.updateByCommentId(bean, commentId);
	}

	/**
	 * 根据CommentId删除
	 */
	@Override
	public Integer deleteForumCommentByCommentId(Integer commentId) {
		return this.forumCommentMapper.deleteByCommentId(commentId);
	}
}