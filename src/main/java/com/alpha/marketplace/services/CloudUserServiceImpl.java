package com.alpha.marketplace.services;

        import com.alpha.marketplace.services.base.CloudUserService;
        import com.google.cloud.storage.*;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

@Service
public class CloudUserServiceImpl implements CloudUserService {
    private final String PROFILE_PICS_URL_PREFIX = "https://storage.googleapis.com/marketplace-user-pics/";

    private final Storage storage;
    private final Bucket profileBucket;

    @Autowired
    public CloudUserServiceImpl(Storage storage) {
        this.storage = storage;
        this.profileBucket = storage.get("marketplace-user-pics");
    }

    @Override
    public Blob saveUserPic(String userId, byte[] bytes){
        String blobName = "user-" + userId;
        String blobPath = PROFILE_PICS_URL_PREFIX + blobName;
        Blob blob = profileBucket.create(blobName, bytes);
        BlobId blobId = blob.getBlobId();
        return blob;
    }
}
