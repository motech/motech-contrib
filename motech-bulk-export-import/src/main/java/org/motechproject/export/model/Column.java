package org.motechproject.export.model;

import org.apache.commons.lang.StringUtils;
import org.motechproject.export.annotation.ExportValue;

import java.lang.reflect.Method;

import static org.apache.commons.lang.StringUtils.*;

public class Column implements Comparable<Column> {

    private Method method;
    private Integer order;

    public Column(Method method) {
        this.method = method;
        order = method.getAnnotation(ExportValue.class).index();
    }

    public String name() {
        String name = nameFromAnnotation(method);
        if (name == null) {
            name = capitalize(method.getName().replace("get", ""));
            return join(splitByCharacterTypeCamelCase(name), " ");
        }
        return name;
    }

    @Override
    public int compareTo(Column o) {
        return this.order.compareTo(o.order);
    }

    public Method getMethod() {
        return method;
    }

    public Object value(Object model) {
        method.setAccessible(true);
        try {
            return method.invoke(model);
        } catch (Exception ignored) {
        }
        return "";
    }

    private String nameFromAnnotation(Method method) {
        ExportValue exportValue = method.getAnnotation(ExportValue.class);
        if (exportValue != null && StringUtils.isNotBlank(exportValue.column())) {
            return exportValue.column();
        }
        return null;
    }
}
