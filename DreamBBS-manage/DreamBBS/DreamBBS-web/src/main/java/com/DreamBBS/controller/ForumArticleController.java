package com.DreamBBS.controller;


import com.DreamBBS.Check.Checklogin;
import com.DreamBBS.config.WebConfig;
import com.DreamBBS.entity.constants.Constants;
import com.DreamBBS.entity.dto.SessionWebUserDto;
import com.DreamBBS.entity.enums.*;
import com.DreamBBS.entity.po.*;
import com.DreamBBS.entity.query.ForumArticleAttachmentQuery;
import com.DreamBBS.entity.query.ForumArticleQuery;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.entity.vo.ResponseVO;
import com.DreamBBS.entity.vo.web.FormArticleDetailVO;
import com.DreamBBS.entity.vo.web.ForumArticleVO;
import com.DreamBBS.exception.BusinessException;
import com.DreamBBS.service.*;
import com.DreamBBS.utils.CopyTools;
import com.DreamBBS.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//文章界面
@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);

    @Resource
    private WebConfig webConfig;

    @Resource
    private ForumBoardService forumBoardService;

    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private LikeRecordService likeRecordService;



    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/loadArticle")
    public ResponseVO loadArticle(HttpSession session, Integer boardId, Integer pBoardId, Integer orderType, Integer pageNo) {
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        //如果板块id为空或者0则默认将板块id设置为空(预留空为首页板块)
        articleQuery.setBoardId(boardId == null || boardId == 0 ? null : boardId);
        articleQuery.setpBoardId(pBoardId);
        articleQuery.setPageNo(pageNo);

        //只显示正常的贴文
        articleQuery.setStatus(ArticleStatusEnum.NORMAL.getStatus());

        //判断文章排序方式,默认为热榜
        ArticleOrderTypeEnum orderTypeEnum = ArticleOrderTypeEnum.getByType(orderType);
        if (orderTypeEnum == null) {
            orderTypeEnum =  ArticleOrderTypeEnum.HOT;
        }
        articleQuery.setOrderBy(orderTypeEnum.getOrderSql());
        //分页
        PaginationResultVO resultVO = forumArticleService.findListByPage(articleQuery);
        return getSuccessResponseVO(convert2PaginationVO(resultVO, ForumArticleVO.class));
    }

    @RequestMapping("/search")
    public ResponseVO loadBoard4Post(String keyword) {
        ForumArticleQuery query = new ForumArticleQuery();
        query.setTitleFuzzy(keyword);
        PaginationResultVO result = forumArticleService.findListByPage(query);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/getArticleDetail")
    public ResponseVO getArticleDetail(HttpSession session, String articleId) {
        SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);

        ForumArticle forumArticle = forumArticleService.readArticle(articleId);

        //当文章被删除或者文章为空时返回错误
        if (forumArticle == null || (ArticleStatusEnum.DEL.getStatus().equals(forumArticle.getStatus()))) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        FormArticleDetailVO detailVO = new FormArticleDetailVO();
        //拷贝(取出)数据库中的文章
        detailVO.setForumArticle(CopyTools.copy(forumArticle, ForumArticleVO.class));

        //判断是否已经点赞
        if (sessionWebUserDto != null) {
            LikeRecord like = likeRecordService.getLikeRecordByObjectIdAndUserIdAndOpType(articleId,sessionWebUserDto.getUserId(),OperRecordOpTypeEnum.ARTICLE_LIKE.getType());
            if (like != null) {
                detailVO.setHaveLike(true);
            }
        }
        return getSuccessResponseVO(detailVO);
    }

    @RequestMapping("/doLike")
    public ResponseVO doLike(HttpSession session,String articleId) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto!=null){
            likeRecordService.doLike(articleId, userDto.getUserId(), userDto.getNickName(), OperRecordOpTypeEnum.ARTICLE_LIKE);
            return getSuccessResponseVO(null);
        }else {
            throw new BusinessException(ResponseCodeEnum.CODE_502);
        }
    }

    @RequestMapping("/loadBoard4Post")
    public ResponseVO loadBoard4Post(HttpSession session) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto!=null){
            Integer postType = null;
            //判断管理员(管理员界面被阉割了不过还是写一下)
            if (!userDto.getAdmin()) {
                postType = 1;
            }
            return getSuccessResponseVO(forumBoardService.getBoardTree(postType));
        }else {
            throw new BusinessException(ResponseCodeEnum.CODE_502);
        }
    }

    @RequestMapping("/postArticle")
     /*
     cover封面,
     pBoardId主板块id
     boardId二级板块id
     title标题
     content内容
     markdownContent md文本编辑器内容
     editorType编辑器类型
     summary摘要
     */
    public ResponseVO postArticle(HttpSession session, MultipartFile cover, Integer pBoardId, Integer boardId, String title, String content, String markdownContent, Integer editorType, String summary) {

        title = StringTools.escapeTitle(title);
        SessionWebUserDto userDto = getUserInfoFromSession(session);

        if(userDto!=null){
            ForumArticle forumArticle = new ForumArticle();
            forumArticle.setpBoardId(pBoardId);
            forumArticle.setBoardId(boardId);
            forumArticle.setTitle(title);
            forumArticle.setContent(content);
            if (EditorTypeEnum.MARKDOWN.getType().equals(editorType) && StringTools.isEmpty(markdownContent)) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            forumArticle.setMarkdownContent(markdownContent);
            forumArticle.setEditorType(editorType);
            forumArticle.setSummary(summary);
            forumArticle.setUserId(userDto.getUserId());
            forumArticle.setNickName(userDto.getNickName());
            forumArticle.setUserIpAddress(userDto.getProvince());

            forumArticleService.postArticle(userDto.getAdmin(),forumArticle, cover);
            return getSuccessResponseVO(forumArticle.getArticleId());
        }else {
            throw new BusinessException(ResponseCodeEnum.CODE_502);
        }

    }

}
