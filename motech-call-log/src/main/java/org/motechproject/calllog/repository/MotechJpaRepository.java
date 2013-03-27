package org.motechproject.calllog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.repository.annotation.RestResource;

//Prevents repositories from exporting update/delete operations as REST APIs
@NoRepositoryBean
public interface MotechJpaRepository<T>  extends JpaRepository<T, Long> {

    @Override
    @RestResource(exported = false)
    void delete(Long id);

    @Override
    @RestResource(exported = false)
    void delete(T entity);

    @Override
    @RestResource(exported = false)
    <S extends T> S save(S entity);
}
