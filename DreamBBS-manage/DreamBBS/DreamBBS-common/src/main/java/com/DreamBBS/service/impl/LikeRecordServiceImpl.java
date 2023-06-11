package com.DreamBBS.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.DreamBBS.entity.enums.*;
import com.DreamBBS.entity.po.ForumArticle;
import com.DreamBBS.entity.po.ForumComment;
import com.DreamBBS.entity.po.UserMessage;
import com.DreamBBS.entity.query.*;
import com.DreamBBS.exception.BusinessException;
import com.DreamBBS.mappers.ForumArticleMapper;
import com.DreamBBS.mappers.ForumCommentMapper;
import com.DreamBBS.mappers.UserMessageMapper;
import org.springframework.stereotype.Service;

import com.DreamBBS.entity.po.LikeRecord;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.mappers.LikeRecordMapper;
import com.DreamBBS.service.LikeRecordService;
import com.DreamBBS.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 点赞记录 业务接口实现
 */
@Service("likeRecordService")
public class LikeRecordServiceImpl implements LikeRecordService {

	@Resource
	private LikeRecordMapper<LikeRecord, LikeRecordQuery> likeRecordMapper;

	@Resource
	private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;

	@Resource
	private ForumCommentMapper<ForumComment, ForumCommentQuery> forumCommentMapper;

	@Resource
	private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<LikeRecord> findListByParam(LikeRecordQuery param) {
		return this.likeRecordMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(LikeRecordQuery param) {
		return this.likeRecordMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<LikeRecord> findListByPage(LikeRecordQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<LikeRecord> list = this.findListByParam(param);
		PaginationResultVO<LikeRecord> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(LikeRecord bean) {
		return this.likeRecordMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<LikeRecord> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.likeRecordMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<LikeRecord> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.likeRecordMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(LikeRecord bean, LikeRecordQuery param) {
		StringTools.checkParam(param);
		return this.likeRecordMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(LikeRecordQuery param) {
		StringTools.checkParam(param);
		return this.likeRecordMapper.deleteByParam(param);
	}

	/**
	 * 根据OpId获取对象
	 */
	@Override
	public LikeRecord getLikeRecordByOpId(Integer opId) {
		return this.likeRecordMapper.selectByOpId(opId);
	}

	/**
	 * 根据OpId修改
	 */
	@Override
	public Integer updateLikeRecordByOpId(LikeRecord bean, Integer opId) {
		return this.likeRecordMapper.updateByOpId(bean, opId);
	}

	/**
	 * 根据OpId删除
	 */
	@Override
	public Integer deleteLikeRecordByOpId(Integer opId) {
		return this.likeRecordMapper.deleteByOpId(opId);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType获取对象
	 */
	@Override
	public LikeRecord getLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opType);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType修改
	 */
	@Override
	public Integer updateLikeRecordByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.updateByObjectIdAndUserIdAndOpType(bean, objectId, userId, opType);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType删除
	 */
	@Override
	public Integer deleteLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opType);
	}

	@Transactional(rollbackFor = Exception.class)
	public void doLike(String objectId, String userId, String nickName, OperRecordOpTypeEnum opTypeEnum) {
		UserMessage userMessage = new UserMessage();
		userMessage.setCreateTime(new Date());
		LikeRecord likeRecord = null;
		//判断点赞种类
		switch (opTypeEnum) {
			case ARTICLE_LIKE:
				likeRecord = articleLike(objectId, userId, opTypeEnum);
				ForumArticle forumArticle = forumArticleMapper.selectByArticleId(objectId);
				userMessage.setArticleId(objectId);
				userMessage.setArticleTitle("用户点赞提醒");
				userMessage.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
				userMessage.setCommentId(0);
				userMessage.setReceivedUserId(forumArticle.getUserId());
				break;
			case COMMENT_LIKE:
				/*likeRecord = commentLike(objectId, userId, opTypeEnum);
				ForumComment forumComment = forumCommentMapper.selectByCommentId(Integer.parseInt(objectId));
				ForumArticle commentArticle = forumArticleMapper.selectByArticleId(forumComment.getArticleId());
				userMessage.setArticleId(commentArticle.getArticleId());
				userMessage.setArticleTitle(commentArticle.getTitle());
				userMessage.setMessageType(MessageTypeEnum.COMMENT_LIKE.getType());
				userMessage.setCommentId(Integer.parseInt(objectId));
				userMessage.setReceivedUserId(forumComment.getUserId());
				userMessage.setMessageContent(forumComment.getContent());*/
				break;
		}
		userMessage.setSendUserId(userId);
		userMessage.setSendNickName(nickName);
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		userMessage.setMessageContent("你的"+opTypeEnum.getDesc()+"收到了来自"+userMessage.getSendNickName()+"的点赞");
		if (!userId.equals(userMessage.getReceivedUserId())) {
			UserMessage dbinfo = userMessageMapper.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(userMessage.getReceivedUserId(),userMessage.getCommentId(),userMessage.getSendUserId(),userMessage.getMessageType());
			if(dbinfo ==null){
				userMessageMapper.insert(userMessage);
			}
		}
	}

	public LikeRecord articleLike(String objectId, String userId, OperRecordOpTypeEnum opTypeEnum) {
		LikeRecord record = this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opTypeEnum.getType());
		if (record != null) {
			//从点赞表中移除
			this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opTypeEnum.getType());
			//更新点赞数
			forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), -1, objectId);
		} else {
			ForumArticle forumArticle = forumArticleMapper.selectByArticleId(objectId);
			if (null == forumArticle) {
				throw new BusinessException("文章不存在");
			}
			//数据插入点赞表
			LikeRecord likeRecord = new LikeRecord();
			likeRecord.setObjectId(objectId);
			likeRecord.setUserId(userId);
			likeRecord.setOpType(opTypeEnum.getType());
			likeRecord.setCreateTime(new Date());
			likeRecord.setAuthorUserId(forumArticle.getUserId());
			this.likeRecordMapper.insert(likeRecord);
			//更新点赞数
			forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), 1, objectId);
		}
		return record;
	}


	//***
	/*public LikeRecord commentLike(String objectId, String userId, OperRecordOpTypeEnum opTypeEnum) {
		LikeRecord record = this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opTypeEnum.getType());
		if (record != null) {
			this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opTypeEnum.getType());
			forumCommentMapper.updateCommenCount(-1, Integer.parseInt(objectId));
		} else {
			ForumComment forumComment = forumCommentMapper.selectByCommentId(Integer.parseInt(objectId));
			if (null == forumComment) {
				throw new BusinessException("评论不存在");
			}
			LikeRecord likeRecord = new LikeRecord();
			likeRecord.setObjectId(objectId);
			likeRecord.setUserId(userId);
			likeRecord.setOpType(opTypeEnum.getType());
			likeRecord.setCreateTime(new Date());
			likeRecord.setAuthorUserId(forumComment.getUserId());
			this.likeRecordMapper.insert(likeRecord);
			forumCommentMapper.updateCommenCount(1, Integer.parseInt(objectId));
		}
		return record;
	}*/


}