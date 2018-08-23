package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;

public interface CloudUserRepository {

    String saveUserPic(String userId, byte[] bytes, String contentType);
    String saveUserPicFromUrl(String userId, String url) throws CannotFetchBytesException;
    String getPROFILE_PICS_URL_PREFIX();
}
