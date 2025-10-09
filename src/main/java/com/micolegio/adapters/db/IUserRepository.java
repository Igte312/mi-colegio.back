package com.micolegio.adapters.db;

import com.micolegio.adapters.db.dto.UserBasicInfo;

public interface IUserRepository {

    UserBasicInfo getUserBasicInfoByEmail(String email);
}
