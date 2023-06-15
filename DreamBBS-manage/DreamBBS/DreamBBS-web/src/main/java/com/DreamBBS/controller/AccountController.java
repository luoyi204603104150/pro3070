package com.DreamBBS.controller;

import com.DreamBBS.entity.constants.Constants;
import com.DreamBBS.entity.dto.SessionWebUserDto;
import com.DreamBBS.entity.po.UserInfo;
import com.DreamBBS.entity.vo.ResponseVO;
import com.DreamBBS.service.UserInfoService;
import com.DreamBBS.service.impl.UserInfoServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    //登入
    @RequestMapping("/login")
    public ResponseVO login(HttpSession session, HttpServletRequest request, String email, String password) {
        SessionWebUserDto sessionWebUserDto = userInfoService.login(email, password, getIpAddr(request));
        session.setAttribute(Constants.SESSION_KEY, sessionWebUserDto);
        return getSuccessResponseVO(null);
    }

    //获取用户信息
    @RequestMapping("/getUserInfo")
    public ResponseVO getUserInfo(HttpSession session) {
        return getSuccessResponseVO(getUserInfoFromSession(session));
    }

    //退出登录
    @RequestMapping("/logout")
    public ResponseVO logout(HttpSession session) {
        session.invalidate();
        return getSuccessResponseVO(null);
    }

    //重置密码
    @RequestMapping("/resetPwd")
    public ResponseVO resetPwd(HttpSession session, String email, String password) {

        userInfoService.resetPwd(email, password);
        return getSuccessResponseVO(null);

    }



}
