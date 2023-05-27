package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.WeChatConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public UserLoginVO login(UserLoginDTO dto) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", dto.getCode());
        map.put("grant_type", WeChatConstant.GRANT_TYPE);
        //根据用户授权码请求微信服务器获取openid
        String result = HttpClientUtil.doGet(WeChatConstant.JSCODE_2_SESSION_URL, map);
        JSONObject parse = (JSONObject) JSONObject.parse(result);
        String openid = parse.getString("openid");
        //根据openid到数据库查询用户信息
        User user = userMapper.findByOpenid(openid);
        //判断用户是否存在
        if (user == null) {
            //若用户不存在  添加该用户到数据库
            //创建用户对象
            user = User.builder()
                    .openid(openid)
                    .build();
            userMapper.insert(user);
        }
        //生成令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        //创建vo对象 并返回
        return UserLoginVO.builder()
                .id(user.getId())
                .openid(openid)
                .token(token)
                .build();
    }
}
