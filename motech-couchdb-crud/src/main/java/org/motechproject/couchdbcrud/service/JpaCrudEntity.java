package org.motechproject.couchdbcrud.service;

import org.motechproject.couchdbcrud.repository.JpaCrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JpaCrudEntity<T,ID extends Serializable> extends CrudEntity<T> {

    public JpaCrudEntity(JpaRepository<T,ID> jpaRepository, Class idClass) {
        super(new JpaCrudRepository<>(jpaRepository, idClass));
    }

    public List<String> getHiddenFields(){
        return new ArrayList<>();
    }

    public Map<String, String> getDefaultValues(){
        return new HashMap<>();
    }
}
