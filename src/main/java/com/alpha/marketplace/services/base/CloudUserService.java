package com.alpha.marketplace.services.base;

import java.util.Map;

public interface CloudUserService {

    String saveUserPic(String userId, byte[] bytes, String contentType);
}
