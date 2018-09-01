package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;

public interface CloudExtensionRepository {
    Blob saveExtension(String userId, String extensionName, String contentType, byte[] bytes);
    Blob saveExtensionFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException;
    Blob updateExtension(BlobId blobId, String userId, String extensionName, String contentType, byte[] bytes);
    Blob saveExtensionPic(String userId, String extensionName, String contentType, byte[] bytes);
    Blob saveExtensionPicFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException;
    boolean delete(BlobId blobid);
}
