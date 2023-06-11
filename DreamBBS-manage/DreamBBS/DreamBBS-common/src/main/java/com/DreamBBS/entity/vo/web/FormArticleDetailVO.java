package com.DreamBBS.entity.vo.web;

public class FormArticleDetailVO {
    private ForumArticleVO forumArticle;
    private Boolean haveLike = false;

    public Boolean getHaveLike() {
        return haveLike;
    }

    public void setHaveLike(Boolean haveLike) {
        this.haveLike = haveLike;
    }

    public ForumArticleVO getForumArticle() {
        return forumArticle;
    }

    public void setForumArticle(ForumArticleVO forumArticle) {
        this.forumArticle = forumArticle;
    }

}
