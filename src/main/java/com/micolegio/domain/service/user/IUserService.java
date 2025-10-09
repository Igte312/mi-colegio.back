package com.micolegio.domain.service.user;

import com.micolegio.adapters.db.dto.UserBasicInfo;

public interface IUserService {

    UserBasicInfo getUserBasicInfoByEmail(String email);
}
