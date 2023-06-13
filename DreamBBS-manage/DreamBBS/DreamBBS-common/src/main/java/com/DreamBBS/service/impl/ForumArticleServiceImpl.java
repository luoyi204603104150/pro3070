package com.DreamBBS.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.DreamBBS.config.AppConfig;
import com.DreamBBS.entity.constants.Constants;
import com.DreamBBS.entity.dto.FileUploadDto;
import com.DreamBBS.entity.enums.*;
import com.DreamBBS.entity.po.ForumBoard;
import com.DreamBBS.exception.BusinessException;
import com.DreamBBS.service.ForumBoardService;
import com.DreamBBS.service.UserInfoService;
import com.DreamBBS.service.UserMessageService;
import com.DreamBBS.utils.FileUtils;
import com.DreamBBS.utils.ImageUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.DreamBBS.entity.query.ForumArticleQuery;
import com.DreamBBS.entity.po.ForumArticle;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.entity.query.SimplePage;
import com.DreamBBS.mappers.ForumArticleMapper;
import com.DreamBBS.service.ForumArticleService;
import com.DreamBBS.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文章信息 业务接口实现
 */
@Service("forumArticleService")
public class ForumArticleServiceImpl implements ForumArticleService {

	@Resource
	private ForumArticleMapper<ForumArticle, ForumArticleQuery> forumArticleMapper;

	@Resource
	private FileUtils fileUtils;

	@Resource
	private ForumBoardService forumBoardService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private AppConfig appConfig;

	@Resource
	private ImageUtils imageUtils;

	@Resource
	private UserMessageService userMessageService;

	@Lazy
	@Resource
	private ForumArticleServiceImpl forumArticleService;
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void postArticle(Boolean isAdmin, ForumArticle article, MultipartFile cover) {
		//基本校验
		checkArticle(isAdmin, article);

		String articleId = StringTools.getRandomString(Constants.LENGTH_15);
		article.setArticleId(articleId);
		article.setPostTime(new Date());
		article.setLastUpdateTime(new Date());

		if (cover != null) {
			FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, FileUploadTypeEnum.ARTICLE_COVER, Constants.FILE_FOLDER_IMAGE);
			article.setCover(fileUploadDto.getLocalPath());
		}
		article.setStatus(ArticleStatusEnum.NORMAL.getStatus());

		//替换图片
		String content = article.getContent();
		if (!StringTools.isEmpty(content)) {
			String month = imageUtils.resetImageHtml(content);
			//避免替换博客中template关键，所以前后带上/
			String replaceMonth = "/" + month + "/";
			content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
			article.setContent(content);
			String markdownContent = article.getMarkdownContent();
			if (!StringTools.isEmpty(markdownContent)) {
				markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
				article.setMarkdownContent(markdownContent);
			}
		}

		this.forumArticleMapper.insert(article);

		//增加积分
		Integer postIntegral = Constants.PostArticle_integral;
		if (postIntegral > 0 && ArticleStatusEnum.NORMAL.getStatus().equals(article.getStatus())) {
			this.userInfoService.updateUserIntegral(article.getUserId(),
					UserIntegralOperTypeEnum.POST_COMMENT, UserIntegralChangeTypeEnum.ADD.getChangeType(), postIntegral);
		}
	}

	private void checkArticle(Boolean isAdmin, ForumArticle article) {
		EditorTypeEnum editorTypeEnum = EditorTypeEnum.getByType(article.getEditorType());
		if (null == editorTypeEnum) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (!StringTools.isEmpty(article.getSummary()) && article.getSummary().length() > Constants.LENGTH_200) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		resetBoardInfo(isAdmin, article);
	}

	private void resetBoardInfo(Boolean isAdmin, ForumArticle article) {
		ForumBoard board = forumBoardService.getForumBoardByBoardId(article.getpBoardId());
		if (null == board || board.getPostType() == 0 && !isAdmin) {
			throw new BusinessException("一级板块不存在");
		}
		article.setpBoardName(board.getBoardName());
		if (article.getBoardId() != null && article.getBoardId() != 0) {
			board = forumBoardService.getForumBoardByBoardId(article.getBoardId());
			if (null == board || board.getPostType() == 0 && !isAdmin) {
				throw new BusinessException("二级板块不存在");
			}
			article.setBoardName(board.getBoardName());
		} else {
			article.setBoardId(0);
			article.setBoardName("");
		}
	}

}