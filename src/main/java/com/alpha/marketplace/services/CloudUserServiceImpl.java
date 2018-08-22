package com.alpha.marketplace.services;

        import com.google.cloud.storage.*;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

@Service
public class CloudUserServiceImpl {
    private final String PROFILE_PICS_URL_PREFIX = "https://storage.googleapis.com/marketplace-user-pics/";

    private final Storage storage;
    private final Bucket profileBucket;

    @Autowired
    public CloudUserServiceImpl(Storage storage) {
        this.storage = storage;
        this.profileBucket = storage.get("marketplace-user-pics");
    }
}
