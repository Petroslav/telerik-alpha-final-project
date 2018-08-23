package com.alpha.marketplace.services;

        import com.alpha.marketplace.services.base.CloudUserService;
        import com.google.cloud.storage.*;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.HashMap;
        import java.util.Map;

@Service
public class CloudUserServiceImpl implements CloudUserService {
    private final String FILE_NAME_PREFIX = "user-";
    private final String PROFILE_PICS_URL_PREFIX = "https://storage.googleapis.com/marketplace-user-pics/";

    private final Storage storage;
    private final Bucket profileBucket;

    @Autowired
    public CloudUserServiceImpl(Storage storage) {
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
}
