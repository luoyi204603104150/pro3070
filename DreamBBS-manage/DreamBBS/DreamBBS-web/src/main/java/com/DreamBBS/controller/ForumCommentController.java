package com.DreamBBS.controller;


import com.DreamBBS.controller.ABaseController;
import com.DreamBBS.entity.constants.Constants;
import com.DreamBBS.entity.dto.SessionWebUserDto;
import com.DreamBBS.entity.enums.*;
import com.DreamBBS.entity.po.ForumComment;
import com.DreamBBS.entity.po.LikeRecord;
import com.DreamBBS.entity.query.ForumCommentQuery;
import com.DreamBBS.entity.vo.ResponseVO;
import com.DreamBBS.exception.BusinessException;
import com.DreamBBS.service.ForumCommentService;
import com.DreamBBS.service.LikeRecordService;
import com.DreamBBS.utils.StringTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class ForumCommentController extends ABaseController {

    @Resource
    private ForumCommentService forumCommentService;

    @Resource
    private LikeRecordService likeRecordService;

    //加载评论
    @RequestMapping("/loadComment")
    public ResponseVO loadComment(HttpSession session,String articleId, Integer pageNo, Integer orderType) {
        ForumCommentQuery commentQuery = new ForumCommentQuery();
        commentQuery.setArticleId(articleId);
        //默认使用热榜(点赞数优先,之后),置顶优先
        String orderBy = orderType == null || orderType == 0 ? "good_count desc,comment_id asc" : "comment_id desc";
        commentQuery.setOrderBy("top_type desc," + orderBy);
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (userDto != null) {
            commentQuery.setQueryLikeType(true);
            commentQuery.setCurrentUserId(userDto.getUserId());
        } else {
            commentQuery.setStatus(CommentStatusEnum.NORMAL.getStatus());
        }
        commentQuery.setPageNo(pageNo);
        commentQuery.setPageSize(PageSize.SIZE50.getSize());
        commentQuery.setpCommentId(0);
        commentQuery.setLoadChildren(true);
        return getSuccessResponseVO(forumCommentService.findListByPage(commentQuery));
    }

    @RequestMapping("/doLike")
    public ResponseVO doLike(HttpSession session,Integer commentId) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        String objectId = String.valueOf(commentId);
        if(userDto!=null){
            //上传数据
            likeRecordService.doLike(objectId,userDto.getUserId(),userDto.getNickName(),OperRecordOpTypeEnum.COMMENT_LIKE);
            //返回信息
            LikeRecord userOperRecord = likeRecordService.getLikeRecordByObjectIdAndUserIdAndOpType(objectId, userDto.getUserId(), OperRecordOpTypeEnum.COMMENT_LIKE.getType());
            ForumComment comment = forumCommentService.getForumCommentByCommentId(commentId);
            comment.setLikeType(userOperRecord == null ? null : 1);
            return getSuccessResponseVO(comment);
        }else {
            throw new BusinessException(ResponseCodeEnum.CODE_502);
        }

    }

    @RequestMapping("/changeTopType")
    public ResponseVO changeTopType(HttpSession session,Integer commentId,Integer topType) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        forumCommentService.changeTopType(userDto.getUserId(), commentId, topType);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/postComment")

    public ResponseVO postComment(HttpSession session, String articleId, Integer pCommentId, String content, MultipartFile image, String replyUserId) {

        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (userDto != null) {
            //单独判断内容和图片
            if (image == null && StringTools.isEmpty(content)) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }

            ForumComment comment = new ForumComment();
            content = StringTools.escapeHtml(content);
            comment.setUserId(userDto.getUserId());
            comment.setNickName(userDto.getNickName());
            comment.setUserIpAddress(userDto.getProvince());
            comment.setpCommentId(pCommentId);
            comment.setArticleId(articleId);
            comment.setContent(content);
            comment.setReplyUserId(replyUserId);
            comment.setTopType(CommentTopTypeEnum.NO_TOP.getType());
            forumCommentService.postComment(comment, image);
            //如果有2级评论会返回所有2级评论
            if (pCommentId != 0) {
                ForumCommentQuery commentQuery = new ForumCommentQuery();
                commentQuery.setArticleId(articleId);
                commentQuery.setpCommentId(pCommentId);
                commentQuery.setOrderBy("top_type desc,comment_id asc");
                commentQuery.setCurrentUserId(userDto.getUserId());
                List<ForumComment> children = forumCommentService.findListByParam(commentQuery);
                return getSuccessResponseVO(children);
            }
            return getSuccessResponseVO(comment);
        } else {
            throw new BusinessException(ResponseCodeEnum.CODE_502);
        }


    }


}
