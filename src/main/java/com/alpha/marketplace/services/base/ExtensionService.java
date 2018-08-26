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
    void createExtension(ExtensionBindingModel extensionBindingModel, BindingResult errors);
    Extension getById(int id);
    List<Extension> getAllApproved();
    void approveExtensionById(int id);
    Extension getByName(String name);
    void sync(int id);
    void delete(int id);
    boolean isUserPublisherOrAdmin(Extension extension);
    User currentUser();
    void reloadLists();
    List<Extension> getUnapproved();
}
