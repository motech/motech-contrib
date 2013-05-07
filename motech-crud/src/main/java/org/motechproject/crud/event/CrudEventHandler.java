package org.motechproject.crud.event;

public interface CrudEventHandler<T> {
    void deleted(T object);
    void updated(T object);
}
