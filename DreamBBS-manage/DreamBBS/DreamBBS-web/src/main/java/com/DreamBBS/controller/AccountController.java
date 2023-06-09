package com.DreamBBS.controller;

import com.DreamBBS.entity.po.UserInfo;
import com.DreamBBS.entity.vo.ResponseVO;
import com.DreamBBS.service.UserInfoService;
import com.DreamBBS.service.impl.UserInfoServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class AccountController extends ABaseController{
    @Resource
    private UserInfoServiceImpl userInfoService;

    //登入注册验证码
    public void checkCode(String test) throws
            IOException {
        //我不会写
    }

    //邮箱验证发送
    public void sendEmailCode(String test) throws
            IOException {
        //被阉割了暂时不写
    }

    //注册
    @RequestMapping("/register")
    public ResponseVO register(HttpSession session,String email,String nickName,String password) {

        userInfoService.register(email,nickName,password);
        return getSuccessResponseVO(null);


    }



}
