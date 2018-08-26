package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.google.cloud.storage.BlobId;

public interface CloudExtensionRepository {
    BlobId saveExtension(String userId, String extensionName, String contentType, byte[] bytes);
    BlobId saveExtensionFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException;
    BlobId updateExtension(BlobId blobId, String userId, String extensionName, String contentType, byte[] bytes);
    String saveExtensionPic(String userId, String extensionName, String contentType, byte[] bytes);
    String saveExtensionPicFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException;
    String getEXTENSION_URL_PREFIX();
    String getEXTENSION_PIC_URL_PREFIX();
    boolean delete(BlobId blobid);
}
