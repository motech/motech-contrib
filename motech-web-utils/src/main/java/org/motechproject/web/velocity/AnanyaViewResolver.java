package org.motechproject.web.velocity;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnanyaViewResolver extends VelocityLayoutViewResolver {

    private Map<String, String> mappings = new HashMap<>();

    public void setMappings(LinkedHashMap<String, String> mappings) {
        this.mappings = mappings;
    }

    protected AbstractUrlBasedView buildView(final String viewName) throws Exception {
        VelocityLayoutView view = (VelocityLayoutView) super.buildView(viewName);
        if (this.mappings.isEmpty()) {
            return view;
        }
        for ( Map.Entry<String, String> entry : this.mappings.entrySet() ) {
            String mappingRegexp = StringUtils.replace(entry.getKey(), "*", ".*");
            if ( viewName.matches(mappingRegexp )) {
                view.setLayoutUrl(entry.getValue());
                return view;
            }
        }
        return view;
    }
}
