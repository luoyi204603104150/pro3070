package com.DreamBBS.service;

import java.util.Date;
import java.util.List;

import com.DreamBBS.entity.po.ForumArticle;
import com.DreamBBS.entity.po.UserInfo;
import com.DreamBBS.entity.query.ForumCommentQuery;
import com.DreamBBS.entity.po.ForumComment;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * 评论 业务接口
 */
public interface ForumCommentService {

	/**
	 * 根据条件查询列表
	 */
	List<ForumComment> findListByParam(ForumCommentQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ForumCommentQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<ForumComment> findListByPage(ForumCommentQuery param);

	/**
	 * 新增
	 */
	Integer add(ForumComment bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ForumComment> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ForumComment> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(ForumComment bean,ForumCommentQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(ForumCommentQuery param);

	/**
	 * 根据CommentId查询对象
	 */
	ForumComment getForumCommentByCommentId(Integer commentId);


	/**
	 * 根据CommentId修改
	 */
	Integer updateForumCommentByCommentId(ForumComment bean,Integer commentId);


	/**
	 * 根据CommentId删除
	 */
	Integer deleteForumCommentByCommentId(Integer commentId);


	void changeTopType(String userId, Integer commentId, Integer topType);

	void postComment(ForumComment comment, MultipartFile file);
}