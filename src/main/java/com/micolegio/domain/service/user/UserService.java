package com.micolegio.domain.service.user;

import com.micolegio.adapters.db.IUserRepository;
import com.micolegio.adapters.db.dto.UserBasicInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final IUserRepository userRepository;

    @Override
    public UserBasicInfo getUserBasicInfoByEmail(String email) {

        return userRepository.getUserBasicInfoByEmail(email);
    }
}
