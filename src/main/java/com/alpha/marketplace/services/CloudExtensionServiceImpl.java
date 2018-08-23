package com.alpha.marketplace.services;


import com.alpha.marketplace.errors.CannotFetchBytesException;
import com.alpha.marketplace.services.base.CloudExtensionService;
import com.alpha.marketplace.utils.Utils;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;


@Service
public class CloudExtensionServiceImpl implements CloudExtensionService {

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
        String name = userId + "-" + extensionName;
        Blob blob = extensionBucket.create(name, bytes, contentType, Bucket.BlobTargetOption.doesNotExist());
        return blob.getBlobId();
    }

    @Override
    public BlobId saveExtensionFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException {
        String name = userId + "-" + extensionName;
        byte[] bytes = Utils.getBytesFromUrl(urlString);
        String contentType = Utils.getContentTypeFromUrl(urlString);

        if(bytes == null || contentType == null){
            throw new CannotFetchBytesException("Unable to save URL");
        }

        Blob blob = extensionBucket.create(name, bytes, contentType);

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
        String name = userId + "-" + extensionName;
        Blob blob = extensionBucket.create(name, bytes, contentType, Bucket.BlobTargetOption.doesNotExist());
        return EXTENSION_URL_PREFIX + blob.getName();
    }

    @Override
    public String saveExtensionPicFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException {
        byte[] bytes = Utils.getBytesFromUrl(urlString);
        String contentType = Utils.getContentTypeFromUrl(urlString);
        if(bytes == null || contentType == null){
            throw new CannotFetchBytesException("Unable to save URL");
        }
        return saveExtensionPic(userId, extensionName, contentType, bytes);
    }

    public String getEXTENSION_URL_PREFIX() {
        return EXTENSION_URL_PREFIX;
    }

    public String getEXTENSION_PIC_URL_PREFIX() {
        return EXTENSION_PIC_URL_PREFIX;
    }
}

