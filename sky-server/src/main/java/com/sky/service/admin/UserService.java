package com.sky.service.admin;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

public interface UserService {
    UserLoginVO login(UserLoginDTO dto);
}
