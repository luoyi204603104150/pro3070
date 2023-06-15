package com.DreamBBS.service;

import java.util.List;

import com.DreamBBS.entity.query.ForumArticleQuery;
import com.DreamBBS.entity.po.ForumArticle;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文章信息 业务接口
 */
public interface ForumArticleService {

	/**
	 * 根据条件查询列表
	 */
	List<ForumArticle> findListByParam(ForumArticleQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ForumArticleQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery param);

	/**
	 * 新增
	 */
	Integer add(ForumArticle bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ForumArticle> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ForumArticle> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(ForumArticle bean,ForumArticleQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(ForumArticleQuery param);

	/**
	 * 根据ArticleId查询对象
	 */
	ForumArticle getForumArticleByArticleId(String articleId);


	/**
	 * 根据ArticleId修改
	 */
	Integer updateForumArticleByArticleId(ForumArticle bean,String articleId);


	/**
	 * 根据ArticleId删除
	 */
	Integer deleteForumArticleByArticleId(String articleId);

    ForumArticle readArticle(String artcileId);

	void postArticle(Boolean isAdmin,ForumArticle article, MultipartFile cover) throws BusinessException;

	void searchArticle();
}