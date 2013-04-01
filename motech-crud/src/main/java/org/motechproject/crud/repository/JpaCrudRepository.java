package org.motechproject.crud.repository;

import org.apache.commons.lang.WordUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

public class JpaCrudRepository<T, ID extends Serializable> implements CrudRepository<T> {

    private JpaRepository<T,ID> jpaRepository;
    private Class idClass;

    public JpaCrudRepository(JpaRepository<T,ID> jpaRepository, Class idClass) {
        this.jpaRepository = jpaRepository;
        this.idClass = idClass;
    }

    public List<T> getAll(final int skip, final int limit) {
        return jpaRepository.findAll(pageable(skip, limit)).getContent();
    }

    public List<T> findBy(String fieldName, String value) {
        Method method = ReflectionUtils.findMethod(jpaRepository.getClass(), "findBy" + WordUtils.capitalize(fieldName), String.class);
        return (List<T>) ReflectionUtils.invokeMethod(method, jpaRepository, value);
    }

    public void save(T object) {
        jpaRepository.save(object);
    }

    @Override
    public T get(String id) {
        Object idValue = convertIdToType(id);
        Method method = ReflectionUtils.findMethod(jpaRepository.getClass(), "findOne", Serializable.class);
        return (T) ReflectionUtils.invokeMethod(method, jpaRepository, idValue);
    }

    private Object convertIdToType(String id) {
        Object idValue;
        if(idClass.equals(Long.class)){
            idValue = Long.parseLong(id);
        } else if(idClass.equals(String.class)) {
            idValue = id;
        } else {
            throw new UnsupportedOperationException("Only String and Long ID types are supported");
        }
        return idValue;
    }

    public int count() {
        return (int) jpaRepository.count();
    }

    @Override
    public void remove(T object) {
        jpaRepository.delete(object);
    }

    private Pageable pageable(final int skip, final int limit) {
        return new Pageable() {
            @Override
            public int getPageNumber() {
                return (skip/limit) + 1;
            }

            @Override
            public int getPageSize() {
                return skip;
            }

            @Override
            public int getOffset() {
                return skip;
            }

            @Override
            public Sort getSort() {
                return null;
            }
        };
    }
}
