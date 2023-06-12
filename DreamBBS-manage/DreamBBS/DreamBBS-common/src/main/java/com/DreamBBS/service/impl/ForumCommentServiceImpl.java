package com.DreamBBS.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.DreamBBS.entity.enums.CommentStatusEnum;
import com.DreamBBS.entity.enums.CommentTopTypeEnum;
import com.DreamBBS.entity.enums.ResponseCodeEnum;
import com.DreamBBS.entity.po.ForumArticle;
import com.DreamBBS.exception.BusinessException;
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
import org.springframework.transaction.annotation.Transactional;


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
			subQuery.setStatus(param.getStatus());

			List<Integer> pcommentIdList = list.stream().map(ForumComment::getCommentId).distinct().collect(Collectors.toList());
			subQuery.setPcommentIdList(pcommentIdList);
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeTopType(String userId, Integer commentId, Integer topType) {
		CommentTopTypeEnum typeEnum = CommentTopTypeEnum.getByType(topType);
		if (null == typeEnum) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		ForumComment forumComment = forumCommentMapper.selectByCommentId(commentId);
		if (forumComment == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		ForumArticle forumArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
		if (forumArticle == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		if (!forumArticle.getUserId().equals(userId) || forumComment.getpCommentId() != 0) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (forumComment.getTopType().equals(topType)) {
			return;
		}

		//置顶
		if (CommentTopTypeEnum.TOP.getType().equals(topType)) {
			forumCommentMapper.updateTopTypeByArticleId(forumComment.getArticleId());
		}

		ForumComment updateInfo = new ForumComment();
		updateInfo.setTopType(topType);
		forumCommentMapper.updateByCommentId(updateInfo, forumComment.getCommentId());
	}

}