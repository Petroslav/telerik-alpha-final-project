package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.models.Extension;

import java.util.Date;
import java.util.List;

public interface ExtensionRepository {

    List<Extension> getAll();
    List<Extension> search(String criteria);
    List<Extension> getByDownloads(long downloads);
    List<Extension> getByUploadDate(Date date);
    List<Extension> getByCommitDate(Date date);
    Extension getById(long id);
    Extension getByName(String name);
    boolean save(Extension extension);
    boolean update(Extension extension);
    boolean updateList(List<Extension> list);
    boolean delete(long id);

}
