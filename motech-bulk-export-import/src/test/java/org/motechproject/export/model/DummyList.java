package org.motechproject.export.model;

import org.motechproject.export.annotation.ComponentTypeProvider;

import java.util.ArrayList;

public class DummyList extends ArrayList<String> {

    public Class<?> componentTypeProvider;

    public DummyList(Class componentTypeProvider) {
        this.componentTypeProvider = componentTypeProvider;
    }

    @ComponentTypeProvider
    public Class getComponentTypeProvider() {
        return componentTypeProvider;
    }
}
