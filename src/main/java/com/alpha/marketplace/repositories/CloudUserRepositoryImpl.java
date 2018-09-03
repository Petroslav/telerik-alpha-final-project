package com.alpha.marketplace.repositories;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.alpha.marketplace.repositories.base.CloudUserRepository;
import com.alpha.marketplace.utils.Utils;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

@Repository
public class CloudUserRepositoryImpl implements CloudUserRepository {
    private final String FILE_NAME_PREFIX = "user-";

    private final Storage storage;
    private final Bucket profileBucket;

    @Autowired
    public CloudUserRepositoryImpl(Storage storage) {
        this.storage = storage;
        this.profileBucket = storage.get("marketplace-user-pics");
    }

    @Override
    public Blob saveUserPic(String userId, byte[] bytes, String contentType){
        String blobName = FILE_NAME_PREFIX + userId;
        return profileBucket.create(blobName, bytes, contentType);
    }

    @Override
    public Blob saveUserPicFromUrl(String userId, String urlString) throws CannotFetchBytesException {
        String blobName = FILE_NAME_PREFIX + userId;
        byte[] bytes = Utils.getBytesFromUrl(urlString);
        String contentType = Utils.getContentTypeFromUrl(urlString);
        if(bytes == null || contentType == null) {
            throw new CannotFetchBytesException("Could not save from URL");
        }

        return profileBucket.create(blobName, bytes, contentType);
    }

    @Override
    public Blob updateUserPic(BlobId blobId, byte[] bytes){
        Blob blob = storage.get(blobId);
        storage.delete(blobId);
        try(WritableByteChannel writer = blob.writer()){
            System.out.println(blob.getMediaLink());
            writer.write(ByteBuffer.wrap(bytes));
            blob.update();
            System.out.println(blob.getMediaLink());
        }catch(IOException e){
            System.out.println("Could not locate file, no changes made");
            e.printStackTrace();
        }
        return blob;
    }

    @Override
    public void deleteUserPic(BlobId bid) {
        storage.delete(bid);
    }
}