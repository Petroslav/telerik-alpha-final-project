package com.alpha.marketplace.services.base;

import com.alpha.marketplace.errors.CannotFetchBytesException;
import com.google.cloud.storage.BlobId;

public interface CloudExtensionService {
    BlobId saveExtension(String userId, String extensionName, String contentType, byte[] bytes);
    BlobId saveExtensionFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException;
    BlobId updateExtension(BlobId blobId, String userId, String extensionName, String contentType, byte[] bytes);
    String saveExtensionPic(String userId, String extensionName, String contentType, byte[] bytes);
    String saveExtensionPicFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException;
    String getEXTENSION_URL_PREFIX();
    String getEXTENSION_PIC_URL_PREFIX();
}
