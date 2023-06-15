package com.DreamBBS.controller;


import com.DreamBBS.controller.ABaseController;
import com.DreamBBS.config.WebConfig;
import com.DreamBBS.entity.constants.Constants;
import com.DreamBBS.entity.dto.SessionWebUserDto;
import com.DreamBBS.entity.enums.OperRecordOpTypeEnum;
import com.DreamBBS.entity.enums.ResponseCodeEnum;
import com.DreamBBS.entity.po.ForumComment;
import com.DreamBBS.entity.po.LikeRecord;
import com.DreamBBS.entity.vo.ResponseVO;
import com.DreamBBS.exception.BusinessException;
import com.DreamBBS.utils.ScaleFilter;
import com.DreamBBS.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";


    @Resource
    private WebConfig webConfig;

    //上传图片
    @RequestMapping("/uploadImage")
    public ResponseVO uploadImage(HttpSession session, MultipartFile file) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(userDto!=null){
            if(file==null){
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            //读取上传的文件名和拓展名
            String fileName = file.getOriginalFilename();
            String fileExtName = StringTools.getFileSuffix(fileName);
            //效验图片类型
            if (!ArrayUtils.contains(Constants.IMAGE_SUFFIX, fileExtName)) {
                throw new BusinessException(ResponseCodeEnum.CODE_501);
            }
            String path = copyFile(file);
            Map<String, String> fileMap = new HashMap();
            fileMap.put("fileName", path);
            return getSuccessResponseVO(fileMap);
        }else {
            throw new BusinessException(ResponseCodeEnum.CODE_502);
        }
    }

    //将图片从前端下载到后端
    private String copyFile(MultipartFile file) {
        try {
            //读取
            String fileName = file.getOriginalFilename();
            String fileExtName = StringTools.getFileSuffix(fileName);

            //输出一个随机值使文件重命名,防止文件名称撞车
            String fileRealName = StringTools.getRandomString(15) + fileExtName;
            //把文件丢到临时目录里
            String folderPath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE +Constants.FILE_FOLDER_TEMP;
            File folder = new File(folderPath);
            //创建临时目录
            if (!folder.exists()) {
                folder.mkdirs();
            }
            //真实路径
            File uploadFile = new File(folderPath + "/" + fileRealName);
            file.transferTo(uploadFile);
            return Constants.FILE_FOLDER_TEMP_2 + "/" + fileRealName;
        } catch (Exception e) {
            logger.error("上传文件失败", e);
            throw new BusinessException(ResponseCodeEnum.CODE_503);
        }
    }

    //传递图片种类和名称
    @RequestMapping("/getImage/{imageFolder}/{imageName}")
    public void getImage(HttpServletResponse response, @PathVariable("imageFolder") String imageFolder, @PathVariable("imageName") String imageName) {
        readImage(response, imageFolder, imageName);
    }

    //获取用户头像
    @RequestMapping("/getAvatar/{userId}")
    public void getAvatar(HttpServletResponse response,@PathVariable("userId") String userId) {
        String avatarFolderName = Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR_NAME;
        //设置文件路径,并且同一图片类型
        String avatarPath = webConfig.getProjectFolder() + avatarFolderName + userId + Constants.AVATAR_SUFFIX;
        //判断文件夹是否存在
        File folder = new File(avatarFolderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(avatarPath);
        String imageFolder = Constants.FILE_FOLDER_AVATAR_NAME;
        String imageName = userId + Constants.AVATAR_SUFFIX;
        if (!file.exists()) {
            //如果文件不存在则使用默认头像
            imageName = Constants.AVATAR_DEFUALT;
        }
        readImage(response, imageFolder, imageName);
    }

    private void readImage(HttpServletResponse response, String imageFolder, String imageName) {
        ServletOutputStream sos = null;
        FileInputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            //效验参数
            if (StringTools.isEmpty(imageFolder) || StringUtils.isBlank(imageName)) {
                return;
            }
            String imageSuffix = StringTools.getFileSuffix(imageName);
            String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_IMAGE + imageFolder + "/" + imageName;
            if (Constants.FILE_FOLDER_TEMP_2.equals(imageFolder)) {
                filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imageFolder + "/" + imageName;
            } else if (imageFolder.contains(Constants.FILE_FOLDER_AVATAR_NAME)) {
                filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imageFolder + imageName;
            }
            //判断文件是否存在
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            imageSuffix = imageSuffix.replace(".", "");
            //设置缓存时间,头像不设置
            if (!Constants.FILE_FOLDER_AVATAR_NAME.equals(imageFolder)) {
                response.setHeader("Cache-Control", "max-age=2592000");
            }
            response.setContentType("image/" + imageSuffix);
            //输出文件流
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            baos = new ByteArrayOutputStream();
            int ch = 0;
            while (-1 != (ch = in.read())) {
                baos.write(ch);
            }
            sos.write(baos.toByteArray());
        } catch (Exception e) {
            logger.error("读取图片异常", e);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
    }

}
