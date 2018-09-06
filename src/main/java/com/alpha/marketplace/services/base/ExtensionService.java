package com.alpha.marketplace.services.base;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Set;

public interface ExtensionService {
    List<Extension> getAll();
    List<Extension> getMostPopular();
    List<Extension> getAdminSelection();
    List<Extension> getLatest();
    List<Extension> getAllApproved();
    List<Extension> getUnapproved();
    Set<Extension> searchExtensions(String criteria);
    Set<Extension> searchExtensionsByTag(String criteria);
    Extension getById(long id);
    Extension getByIdFromMemory(long id);
    Extension getByName(String name);
    User currentUser();
    boolean isUserPublisherOrAdmin(Extension extension);
    boolean update(Extension extension);
    void unfeatureList(List<Long> extensions);
    void createExtension(ExtensionBindingModel extensionBindingModel, BindingResult errors);
    void delete(long id);
    void approveExtensionById(long id);
    void disapproveExtensionById(long id);
    void reloadLists();
    void sync(long id);
    void setSync(long period);
    void download(long id);
    void setFeatured(long id);
    void removeFeatured(long id);
}
