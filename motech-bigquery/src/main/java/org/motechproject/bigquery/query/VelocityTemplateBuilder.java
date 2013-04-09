package org.motechproject.bigquery.query;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.motechproject.bigquery.model.FilterParams;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.io.StringWriter;

@Component
public class VelocityTemplateBuilder {

    private RuntimeInstance velocityRuntime;

    public VelocityTemplateBuilder() {
        setUpVelocityRuntime();
    }

    public String evaluate(String sqlTemplate, FilterParams filterParams) {
        StringWriter out = new StringWriter(sqlTemplate.length());
        SimpleNode nodeTree;
        try {
            nodeTree = velocityRuntime.parse(new StringReader(sqlTemplate), sqlTemplate);
        } catch (Exception pex) {
            throw new RuntimeException("Error parsing template '"
                    + sqlTemplate
                    + "' : "
                    + pex.getMessage());
        }

        if (nodeTree == null) {
            throw new RuntimeException("Error parsing template " + sqlTemplate);
        }

        InternalContextAdapterImpl ica = new InternalContextAdapterImpl(new VelocityContext(filterParams));
        ica.pushCurrentTemplateName(sqlTemplate);
        try {
            nodeTree.init(ica, velocityRuntime);
            nodeTree.render(ica, out);
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ica.popCurrentTemplateName();
        }
    }


    private void setUpVelocityRuntime() {
        velocityRuntime = new RuntimeInstance();
        velocityRuntime.addProperty("userdirective", ChainDirective.class.getName());
        velocityRuntime.addProperty("userdirective", ChunkDirective.class.getName());
        try {
            velocityRuntime.init();
        } catch (Exception ex) {
            throw new RuntimeException("Error setting up Velocity RuntimeInstance.", ex);
        }
    }
}
