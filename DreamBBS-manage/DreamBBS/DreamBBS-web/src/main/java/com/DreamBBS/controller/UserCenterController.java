package com.DreamBBS.controller;

import com.DreamBBS.controller.ABaseController;
import com.DreamBBS.entity.dto.SessionWebUserDto;
import com.DreamBBS.entity.enums.*;
import com.DreamBBS.entity.po.ForumArticle;
import com.DreamBBS.entity.po.UserInfo;
import com.DreamBBS.entity.query.ForumArticleQuery;
import com.DreamBBS.entity.query.LikeRecordQuery;
import com.DreamBBS.entity.query.UserIntegralRecordQuery;
import com.DreamBBS.entity.query.UserMessageQuery;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.entity.vo.ResponseVO;
import com.DreamBBS.entity.vo.web.ForumArticleVO;
import com.DreamBBS.entity.vo.web.UserInfoVO;
import com.DreamBBS.exception.BusinessException;
import com.DreamBBS.service.*;
import com.DreamBBS.utils.CopyTools;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.http.HttpSession;

@RestController("userCenterController")
@RequestMapping("/ucenter")
public class UserCenterController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private UserMessageService userMessageService;

    @Resource
    private LikeRecordService likeRecordService;

    @Resource
    private UserIntegralRecordService userIntegralRecordService;

    @RequestMapping("/getUserInfo")
    public ResponseVO getUserInfo(String userId) {

        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (null == userInfo || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        articleQuery.setUserId(userId);
        articleQuery.setStatus(ArticleStatusEnum.NORMAL.getStatus());
        Integer postCount = forumArticleService.findCountByParam(articleQuery);
        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
        userInfoVO.setLastLoginIpAddress(userInfo.getLastLoginIpAddress());
        userInfoVO.setPostCount(postCount);

        LikeRecordQuery recordQuery = new LikeRecordQuery();
        recordQuery.setAuthorUserId(userId);
        Integer likeCount = likeRecordService.findCountByParam(recordQuery);
        userInfoVO.setLikeCount(likeCount);
        userInfoVO.setCurrentIntegral(userInfo.getCurrentIntegral());
        return getSuccessResponseVO(userInfoVO);


    }

    @RequestMapping("/updateUserInfo")
    public ResponseVO updateUserInfo(HttpSession session, Integer sex, String personDescription, MultipartFile avatar) {

        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto!=null){
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userDto.getUserId());
            userInfo.setSex(sex);
            userInfo.setPersonDescription(personDescription);
            userInfoService.updateUserInfo(userInfo, avatar);
            return getSuccessResponseVO(null);
        }else {
            throw new BusinessException(ResponseCodeEnum.CODE_502);
        }
    }

    @RequestMapping("/loadUserArticle")
    public ResponseVO loadUserArticle(HttpSession session, Integer type, Integer paegNo) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto!=null){
            String userId = userDto.getUserId();
            UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
            if (null == userInfo || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
                throw new BusinessException(ResponseCodeEnum.CODE_404);
            }
            ForumArticleQuery articleQuery = new ForumArticleQuery();
            articleQuery.setOrderBy("post_time desc");
            if (type == 0) {
                articleQuery.setUserId(userId);
            } else if (type == 1) {
                articleQuery.setCommentUserId(userId);
            } else if (type == 2) {
                articleQuery.setLikeUserId(userId);
            }
            if (userDto != null) {
                articleQuery.setCurrentUserId(userDto.getUserId());
            } else {
                articleQuery.setStatus(ArticleStatusEnum.NORMAL.getStatus());
            }
            articleQuery.setPageNo(paegNo);
            PaginationResultVO<ForumArticle> result = forumArticleService.findListByPage(articleQuery);
            return getSuccessResponseVO(convert2PaginationVO(result, ForumArticleVO.class));
        }else {
            throw new BusinessException(ResponseCodeEnum.CODE_502);
        }
    }

}
