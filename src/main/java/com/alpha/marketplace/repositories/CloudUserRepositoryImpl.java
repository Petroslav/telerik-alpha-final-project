package com.alpha.marketplace.repositories;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.alpha.marketplace.repositories.base.CloudUserRepository;
import com.alpha.marketplace.utils.Utils;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CloudUserRepositoryImpl implements CloudUserRepository {
    private final String FILE_NAME_PREFIX = "user-";
    private final String PROFILE_PICS_URL_PREFIX = "https://storage.googleapis.com/marketplace-user-pics/";

    private final Storage storage;
    private final Bucket profileBucket;

    @Autowired
    public CloudUserRepositoryImpl(Storage storage) {
        this.storage = storage;
        this.profileBucket = storage.get("marketplace-user-pics");
    }

    @Override
    public String saveUserPic(String userId, byte[] bytes, String contentType){
        String blobName = FILE_NAME_PREFIX + userId;
        String blobPath = PROFILE_PICS_URL_PREFIX + blobName;
        profileBucket.create(blobName, bytes, contentType);
        return blobPath;
    }

    @Override
    public String saveUserPicFromUrl(String userId, String urlString) throws CannotFetchBytesException {
        String blobName = FILE_NAME_PREFIX + userId;
        String blobPath = PROFILE_PICS_URL_PREFIX + blobName;
        byte[] bytes = Utils.getBytesFromUrl(urlString);
        String contentType = Utils.getContentTypeFromUrl(urlString);
        if(bytes == null || contentType == null) {
            throw new CannotFetchBytesException("Could not save from URL");
        }
        profileBucket.create(blobName, bytes, contentType);

        return blobPath;
    }

    @Override
    public String getPROFILE_PICS_URL_PREFIX() {
        return PROFILE_PICS_URL_PREFIX;
    }
}