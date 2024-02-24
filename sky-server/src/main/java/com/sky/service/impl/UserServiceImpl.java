package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    // 微信开发者信息
    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    UserMapper userMapper;

    /**
     * @return com.sky.entity.User
     * @Description 微信登录
     * @Date 2024/2/24 17:33
     * @Param [userLoginDTO]
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 获得微信用户的唯一标识
        String openid = getOpenid(userLoginDTO);
        // 若 openid 为空，登录失败
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 判断当前用户是否为新用户
        User user = userMapper.getByOpenId(openid);
        if (user == null) {
            user = User.builder().openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        // 返回用户对象
        return user;
    }
    /**
     * @Description 获得微信用户的唯一标识 openid
     * @Date 2024/2/24 18:08
     * @Param [userLoginDTO]
     * @return java.lang.String
     */
    private String getOpenid(UserLoginDTO userLoginDTO) {
        // 封装请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", userLoginDTO.getCode());
        paramMap.put("grant_type", "authorization_code");
        // 向微信接口服务发送请求
        String jsonString = HttpClientUtil.doGet(WX_LOGIN, paramMap);
        // 解析接收的json字符串
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String openid = jsonObject.getString("openid");
        // 返回openid
        return openid;
    }
}
