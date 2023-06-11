package com.DreamBBS.controller;

import com.DreamBBS.controller.ABaseController;
import com.DreamBBS.entity.vo.ResponseVO;
import com.DreamBBS.service.ForumBoardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


//板块
@RestController
@RequestMapping("/board")
public class ForumBoardController extends ABaseController {

    @Resource
    private ForumBoardService forumBoardService;

    //加载板块信息
    @RequestMapping("/loadBoard")
    public ResponseVO loadBoard() {
        //null查询所有板块
        return getSuccessResponseVO(forumBoardService.getBoardTree(null));
    }
}
