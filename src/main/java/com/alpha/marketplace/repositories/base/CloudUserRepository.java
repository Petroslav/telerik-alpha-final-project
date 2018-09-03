package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;

public interface CloudUserRepository {

    Blob saveUserPic(String userId, byte[] bytes, String contentType);
    Blob saveUserPicFromUrl(String userId, String url) throws CannotFetchBytesException;
    Blob updateUserPic(BlobId blobId, byte[] bytes);
    void deleteUserPic(BlobId bid);
}
