package com.alpha.marketplace.services;


import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;


@Service
public class CloudExtensionServiceImpl {

    private final Storage storage;

    private final String EXTENSION_URL_PREFIX = "https://storage.googleapis.com/marketplace-extensions/";
    private final String EXTENSION_PIC_URL_PREFIX = "https://storage.googleapis.com/marketplace-extensions-pics/";

    private final Bucket extensionBucket;
    private final Bucket extensionPicBucket;

    public CloudExtensionServiceImpl(Storage storage) {
        //TODO REFACTOR AUTIWIRING FOR PROPER DI;
        this.storage = storage;
        this.extensionBucket = storage.get("marketplace-extensions");
        this.extensionPicBucket = storage.get("marketplace-extensions-pics");
    }

    public void test(){
    }




}
