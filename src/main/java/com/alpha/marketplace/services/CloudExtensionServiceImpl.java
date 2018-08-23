package com.alpha.marketplace.services;


import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;


@Service
public class CloudExtensionServiceImpl {

    private final Storage storage;

    private final String EXTENSION_URL_PREFIX = "https://storage.googleapis.com/marketplace-extensions/";
    private final String EXTENSION_PIC_URL_PREFIX = "https://storage.googleapis.com/marketplace-extensions-pics/";

    private final Bucket extensionBucket;
    private final Bucket extensionPicBucket;

    @Autowired
    public CloudExtensionServiceImpl(Storage storage) {
        //TODO REFACTOR AUTIWIRING FOR PROPER DI;
        this.storage = storage;
        this.extensionBucket = storage.get("marketplace-extensions");
        this.extensionPicBucket = storage.get("marketplace-extensions-pics");
    }

    public BlobId saveExtension(String userId, String extensionName, String contentType, byte[] bytes){
        String name = userId + " " + extensionName;
        Blob blob = extensionBucket.create(name, bytes, contentType, Bucket.BlobTargetOption.doesNotExist());
        return blob.getBlobId();
    }

    public BlobId updateExtension(BlobId blobId, String userId, String extensionName, String contentType, byte[] bytes){
        Blob blob = storage.get(blobId);
        try{
            blob.writer().write(ByteBuffer.wrap(bytes));
        }catch(IOException e){
            System.out.println("Could not locate file, no changes made");
            e.printStackTrace();

        }
        return blob.getBlobId();
    }

    public String saveExtensionPic(String userId, String extensionName, String contentType, byte[] bytes){
        String name = userId + " " + extensionName;
        Blob blob = extensionBucket.create(name, bytes, contentType, Bucket.BlobTargetOption.doesNotExist());
        return EXTENSION_URL_PREFIX + blob.getName();
    }

    public String getEXTENSION_URL_PREFIX() {
        return EXTENSION_URL_PREFIX;
    }

    public String getEXTENSION_PIC_URL_PREFIX() {
        return EXTENSION_PIC_URL_PREFIX;
    }
}

