package com.alpha.marketplace.services.base;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ExtensionService {
    List<Extension> getMostPopular();
    List<Extension> getAdminSelection();
    List<Extension> getLatest();
    List<Extension> getAllApproved();
    List<Extension> getUnapproved();
    List<Extension> searchExtensions(String criteria);
    List<Extension> searchExtensionsByTag(String criteria);
    Extension getById(int id);
    Extension getByName(String name);
    User currentUser();
    boolean isUserPublisherOrAdmin(Extension extension);
    void createExtension(ExtensionBindingModel extensionBindingModel, BindingResult errors);
    void delete(int id);
    void approveExtensionById(int id);
    void reloadLists();
    void sync(int id);
    void setSync(long period);
    void download(int id);
}
