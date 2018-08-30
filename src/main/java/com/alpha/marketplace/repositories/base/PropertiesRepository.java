package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.models.Properties;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertiesRepository{
    Properties get();
    void update(Properties properties);
}
