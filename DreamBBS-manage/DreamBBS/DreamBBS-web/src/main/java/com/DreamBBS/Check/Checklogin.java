package com.DreamBBS.Check;

import com.DreamBBS.controller.ForumArticleController;
import com.DreamBBS.entity.constants.Constants;
import com.DreamBBS.entity.dto.SessionWebUserDto;
import com.DreamBBS.entity.enums.ResponseCodeEnum;
import com.DreamBBS.exception.BusinessException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class Checklogin {
    private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);
    public Object Checklogin (){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            Object obj = session.getAttribute("session_key");
            return obj;
        }catch (BusinessException e){
            logger.error("登录拦截异常", e);
        }
        return null;
    }
}
