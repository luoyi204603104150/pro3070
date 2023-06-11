package com.DreamBBS.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.DreamBBS.entity.enums.ArticleStatusEnum;
import com.DreamBBS.entity.enums.ResponseCodeEnum;
import com.DreamBBS.entity.enums.UpdateArticleCountTypeEnum;
import com.DreamBBS.exception.BusinessException;
import org.springframework.stereotype.Service;

import com.DreamBBS.entity.enums.PageSize;
import com.DreamBBS.entity.query.ForumArticleQuery;
import com.DreamBBS.entity.po.ForumArticle;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.entity.query.SimplePage;
import com.DreamBBS.mappers.ForumArticleMapper;
import com.DreamBBS.service.ForumArticleService;
import com.DreamBBS.utils.StringTools;


/**
 * 文章信息 业务接口实现
 */
@Service("forumArticleService")
public class ForumArticleServiceImpl implements ForumArticleService {

	@Resource
	private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<ForumArticle> findListByParam(ForumArticleQuery param) {
		return this.forumArticleMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(ForumArticleQuery param) {
		return this.forumArticleMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<ForumArticle> list = this.findListByParam(param);
		PaginationResultVO<ForumArticle> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(ForumArticle bean) {
		return this.forumArticleMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<ForumArticle> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumArticleMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<ForumArticle> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumArticleMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(ForumArticle bean, ForumArticleQuery param) {
		StringTools.checkParam(param);
		return this.forumArticleMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(ForumArticleQuery param) {
		StringTools.checkParam(param);
		return this.forumArticleMapper.deleteByParam(param);
	}

	/**
	 * 根据ArticleId获取对象
	 */
	@Override
	public ForumArticle getForumArticleByArticleId(String articleId) {
		return this.forumArticleMapper.selectByArticleId(articleId);
	}

	/**
	 * 根据ArticleId修改
	 */
	@Override
	public Integer updateForumArticleByArticleId(ForumArticle bean, String articleId) {
		return this.forumArticleMapper.updateByArticleId(bean, articleId);
	}

	/**
	 * 根据ArticleId删除
	 */
	@Override
	public Integer deleteForumArticleByArticleId(String articleId) {
		return this.forumArticleMapper.deleteByArticleId(articleId);
	}
	@Override
	public ForumArticle readArticle(String artcileId) {
		ForumArticle forumArticle = this.forumArticleMapper.selectByArticleId(artcileId);
		//效验参数
		if (forumArticle == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_404);
		}
		//只读正常帖子,并且增加阅读数
		if (ArticleStatusEnum.NORMAL.getStatus().equals(forumArticle.getStatus())) {
			forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.READ_COUNT.getType(), 1, artcileId);
		}
		return forumArticle;
	}
}