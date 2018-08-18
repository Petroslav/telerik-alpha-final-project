package com.alpha.marketplace.repositories;

import com.alpha.marketplace.models.Extension;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Extension, Long> {

}
