package com.alpha.marketplace.services.base;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.models.edit.ExtensionEditModel;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
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
    boolean edit(ExtensionEditModel model, long id, UserService userService);
    void unfeatureList(List<Long> extensions);
    void approveList(List<Long> ids);
    void createExtension(ExtensionBindingModel extensionBindingModel, BindingResult errors, User user, UserService userService);
    Extension createExtension(User publisher, ExtensionBindingModel extensionBindingModel, BindingResult errors, UserService userService);
    void delete(long id, UserService userService);
    void approveExtensionById(long id, UserService userService);
    void disapproveExtensionById(long id, UserService userService);
    void reloadLists();
    void sync(long id);
    void setSync(long period);
    void download(long id);
    void setFeatured(long id);
    void removeFeatured(long id);
    void syncAllExtensions();
}
