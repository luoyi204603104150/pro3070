package com.DreamBBS.service.impl;

import java.beans.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.DreamBBS.config.WebConfig;
import com.DreamBBS.entity.constants.Constants;
import com.DreamBBS.entity.dto.SessionWebUserDto;
import com.DreamBBS.entity.enums.*;
import com.DreamBBS.entity.po.UserIntegralRecord;
import com.DreamBBS.entity.po.UserMessage;
import com.DreamBBS.entity.query.UserIntegralRecordQuery;
import com.DreamBBS.entity.query.UserMessageQuery;
import com.DreamBBS.exception.BusinessException;
import com.DreamBBS.mappers.UserIntegralRecordMapper;
import com.DreamBBS.mappers.UserMessageMapper;
import com.DreamBBS.utils.JsonUtils;
import com.DreamBBS.utils.OKHttpUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.DreamBBS.entity.query.UserInfoQuery;
import com.DreamBBS.entity.po.UserInfo;
import com.DreamBBS.entity.vo.PaginationResultVO;
import com.DreamBBS.entity.query.SimplePage;
import com.DreamBBS.mappers.UserInfoMapper;
import com.DreamBBS.service.UserInfoService;
import com.DreamBBS.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;



/**
 * 用户信息 业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
	//调用日志
	private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);
	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

	@Resource
	private UserIntegralRecordMapper<UserIntegralRecord, UserIntegralRecordQuery> userIntegralRecordMapper;

	@Resource
	private WebConfig webConfig;


	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserInfo> findListByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserInfo> list = this.findListByParam(param);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserInfo bean, UserInfoQuery param) {
		StringTools.checkParam(param);
		return this.userInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserInfoQuery param) {
		StringTools.checkParam(param);
		return this.userInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据UserId获取对象
	 */
	@Override
	public UserInfo getUserInfoByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId修改
	 */
	@Override
	public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
		return this.userInfoMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteUserInfoByUserId(String userId) {
		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Email获取对象
	 */
	@Override
	public UserInfo getUserInfoByEmail(String email) {
		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 * 根据Email修改
	 */
	@Override
	public Integer updateUserInfoByEmail(UserInfo bean, String email) {
		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public Integer deleteUserInfoByEmail(String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}

	/**
	 * 根据NickName获取对象
	 */
	@Override
	public UserInfo getUserInfoByNickName(String nickName) {
		return this.userInfoMapper.selectByNickName(nickName);
	}

	/**
	 * 根据NickName修改
	 */
	@Override
	public Integer updateUserInfoByNickName(UserInfo bean, String nickName) {
		return this.userInfoMapper.updateByNickName(bean, nickName);
	}

	/**
	 * 根据NickName删除
	 */
	@Override
	public Integer deleteUserInfoByNickName(String nickName) {
		return this.userInfoMapper.deleteByNickName(nickName);
	}

	//检测数据,抛出异常回滚数据
	@Transactional(rollbackFor = Exception.class)

	//注册
	public void register(String email,String nickName,String password){
		//效验是否有相同邮箱和昵称
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if (null != userInfo) {
			throw new BusinessException("邮箱账号已经存在");
		}
		UserInfo nickNameUser = this.userInfoMapper.selectByNickName(nickName);
		if (null != nickNameUser) {
			throw new BusinessException("昵称已经存在");
		}

		//插入
		String userId = StringTools.getRandomNumber(10);
		userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setNickName(nickName);
		userInfo.setEmail(email);
		userInfo.setPassword(password);
		userInfo.setJoinTime(new Date());
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		userInfo.setTotalIntegral(0);
		userInfo.setCurrentIntegral(0);
		this.userInfoMapper.insert(userInfo);


		//更新用户积分
		updateUserIntegral(userId,UserIntegralOperTypeEnum.REGISTER,UserIntegralChangeTypeEnum.ADD.getChangeType(),Constants.integral_5);
		//发送信息
		UserMessage userMessage = new UserMessage();
		userMessage.setReceivedUserId(userId);
		userMessage.setMessageType(MessageTypeEnum.SYS.getType());
		userMessage.setCreateTime(new Date());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		userMessage.setMessageContent("感觉画质不如原神");
		userMessageMapper.insert(userMessage);
	}


	//用户积分更新
	@Transactional(rollbackFor = Exception.class)
	public void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum, Integer changeType, Integer integral) {
		integral = changeType * integral;

		if (integral == 0) {
			return;
		}

		UserInfo userInfo = userInfoMapper.selectByUserId(userId);
		if (UserIntegralChangeTypeEnum.REDUCE.getChangeType().equals(changeType) && userInfo.getCurrentIntegral() + integral < 0) {
			integral = changeType * userInfo.getCurrentIntegral();
		}

		//检查变动类型,如果是扣除积分,变量乘-1
		UserIntegralRecord record = new UserIntegralRecord();
		record.setUserId(userId);
		record.setOperType(operTypeEnum.getOperType());
		record.setCreateTime(new Date());
		record.setIntegral(integral);
		this.userIntegralRecordMapper.insert(record);

		Integer count = this.userInfoMapper.updateIntegral(userId, integral);
		if (count == 0) {
			throw new BusinessException("更新用户积分失败");
		}
	}

	//用户登入
	@Override
	public SessionWebUserDto login(String email, String password, String ip) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		//如果未查找到账号或者密码错误抛出异常
		if (null == userInfo || !userInfo.getPassword().equals(password)) {
			throw new BusinessException("账号或者密码错误");
		}
		//如果账号状态为1则提示禁用
		if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
			throw new BusinessException("账号已禁用");
		}

		UserInfo updateInfo = new UserInfo();
		updateInfo.setLastLoginTime(new Date());
		updateInfo.setLastLoginIp(ip);

		Map<String, String> addressInfo = getIpAddress(ip);
		String pro = addressInfo.get("pro");
		pro = StringTools.isEmpty(pro) ? Constants.PRO_UNKNOWN : pro;
		updateInfo.setLastLoginIpAddress(pro);
		this.userInfoMapper.updateByUserId(updateInfo, userInfo.getUserId());
		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setNickName(userInfo.getNickName());
		sessionWebUserDto.setProvince(pro);
		sessionWebUserDto.setUserId(userInfo.getUserId());

		//判断管理员邮箱设置不为空且用户邮箱与管理员邮箱设置相同
		if (!StringTools.isEmpty(webConfig.getAdminEmails()) && ArrayUtils.contains(webConfig.getAdminEmails().split(","), userInfo.getEmail())) {
			sessionWebUserDto.setAdmin(true);
		} else {
			sessionWebUserDto.setAdmin(false);
		}
		return sessionWebUserDto;
	}


	//获取ip

	public Map<String, String> getIpAddress(String ip) {
		Map<String, String> addressInfo = new HashMap<>();
		try {
			String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=" + ip;
			String responseJson = OKHttpUtils.getRequest(url);
			if (StringTools.isEmpty(responseJson)) {
				return addressInfo;
			}
			addressInfo = JsonUtils.convertJson2Obj(responseJson, Map.class);
			return addressInfo;
		} catch (Exception e) {
			logger.error("获取ip所在地失败");
		}
		return addressInfo;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void resetPwd(String email, String password) {
		//选择邮箱
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if (null == userInfo) {
			throw new BusinessException("邮箱账号不存在");
		}

		//更新密码
		UserInfo updateInfo = new UserInfo();
		updateInfo.setPassword(password);
		//同步数据
		this.userInfoMapper.updateByEmail(updateInfo, email);
	}




}