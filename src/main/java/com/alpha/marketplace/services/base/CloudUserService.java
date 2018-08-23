package com.alpha.marketplace.services.base;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;

public interface CloudUserService {

    String saveUserPic(String userId, byte[] bytes, String contentType);
    String saveUserPicFromUrl(String userId, String url) throws CannotFetchBytesException;
    String getPROFILE_PICS_URL_PREFIX();
}
