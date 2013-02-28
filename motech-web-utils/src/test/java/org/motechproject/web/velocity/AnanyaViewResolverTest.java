package org.motechproject.web.velocity;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class AnanyaViewResolverTest {

    @Test
    public void shouldReturnLayoutForAView() throws Exception {
        AnanyaViewResolver viewResolver = new AnanyaViewResolver();
        viewResolver.setMappings(new LinkedHashMap<String, String>() {{
            put("admin/home1", "layout1");
            put("admin/home2*", "layout2");
        }});

        VelocityLayoutView view = (VelocityLayoutView) viewResolver.buildView("admin/home2/hello");

        assertEquals("layout2", ReflectionTestUtils.getField(view, "layoutUrl"));
    }

    @Test
    public void shouldConsiderOrderWhileFindingLayoutUrl() throws Exception {
        AnanyaViewResolver viewResolver = new AnanyaViewResolver();
        viewResolver.setMappings(new LinkedHashMap<String, String>() {{
            put("admin/home1/*", "layout1");
            put("admin/home2*", "layout2");
            put("admin/home*", "layout3");
        }});

        VelocityLayoutView view = (VelocityLayoutView) viewResolver.buildView("admin/home2/hello");

        assertEquals("layout2", ReflectionTestUtils.getField(view, "layoutUrl"));
    }
}
