package com.alpha.marketplace.services.base;

import com.google.cloud.storage.Blob;

public interface CloudUserService {

    Blob saveUserPic(String userId, byte[] bytes);
}
