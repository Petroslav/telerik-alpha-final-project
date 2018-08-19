package com.alpha.marketplace.services;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;

import java.util.List;

public interface ExtensionService {
    List<Extension> getMostPopular();
    List<Extension> getAdminSelection();
    List<Extension> getLatest();
    void createExtension(ExtensionBindingModel extensionBindingModel);
    Extension getById(int id);
    List<Extension> getAllApproved();
    void approveExtensionById(int id);
}
