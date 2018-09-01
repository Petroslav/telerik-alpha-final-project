package com.alpha.marketplace.repositories;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.repositories.base.CloudExtensionRepository;
import com.alpha.marketplace.utils.Utils;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.ByteBuffer;


@Repository
public class CloudExtensionRepositoryImpl implements CloudExtensionRepository {

    private final Storage storage;

    private final Bucket extensionBucket;
    private final Bucket extensionPicBucket;

    @Autowired
    public CloudExtensionRepositoryImpl(Storage storage) {
        this.storage = storage;
        this.extensionBucket = storage.get("marketplace-extensions");
        this.extensionPicBucket = storage.get("marketplace-extension-pics");
    }

    @Override
    public Blob saveExtension(String userId, String extensionName, String contentType, byte[] bytes){
        String name = userId + "-" + extensionName;
        Blob blob = extensionBucket.create(name, bytes, contentType);
        System.out.println(blob.getMediaLink());
        return blob;
    }

    @Override
    public Blob saveExtensionFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException {
        String name = userId + "-" + extensionName;
        byte[] bytes = Utils.getBytesFromUrl(urlString);
        String contentType = Utils.getContentTypeFromUrl(urlString);

        if(bytes == null || contentType == null){
            throw new CannotFetchBytesException("Unable to save URL");
        }

        return extensionBucket.create(name, bytes, contentType);
    }

    @Override
    public Blob updateExtension(BlobId blobId, String userId, String extensionName, String contentType, byte[] bytes){
        Blob blob = storage.get(blobId);
        try{
            blob.writer().write(ByteBuffer.wrap(bytes));
        }catch(IOException e){
            System.out.println("Could not locate file, no changes made");
            e.printStackTrace();
        }
        return blob;
    }

    @Override
    public boolean delete(BlobId blobid) {
        return storage.delete(blobid);
    }

    @Override
    public Blob saveExtensionPic(String userId, String extensionName, String contentType, byte[] bytes){
        String name = userId + "-" + extensionName;
        return extensionPicBucket.create(name, bytes, contentType);
    }

    @Override
    public Blob saveExtensionPicFromUrl(String userId, String extensionName, String urlString) throws CannotFetchBytesException {
        byte[] bytes = Utils.getBytesFromUrl(urlString);
        String contentType = Utils.getContentTypeFromUrl(urlString);
        if(bytes == null || contentType == null){
            throw new CannotFetchBytesException("Unable to save URL");
        }
        return saveExtensionPic(userId, extensionName, contentType, bytes);
    }

}

