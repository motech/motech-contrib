package org.ei.commcare.api.contract;

import org.junit.Test;
import org.motechproject.dao.MotechJsonReader;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class CommCareModuleDefinitionsTest {
    @Test
    public void shouldBeAbleToReadModuleDefinitionsFromJSON() {
        CommCareModuleDefinitions modules = (CommCareModuleDefinitions) new MotechJsonReader().readFromFile(
                "/test-data/commcare-export.json", CommCareModuleDefinitions.class);

        assertThat(modules.userName(), is("someUser@gmail.com"));
        assertThat(modules.password(), is("somePassword"));

        assertThat(modules.modules().size(), is(2));

        CommCareModuleDefinition firstModule = modules.modules().get(0);
        CommCareModuleDefinition secondModule = modules.modules().get(1);

        List<CommCareFormDefinition> definitionsOfFirstModule = firstModule.definitions();
        assertThat(definitionsOfFirstModule.size(), is(2));

        List<CommCareFormDefinition> definitionsOfSecondModule = secondModule.definitions();
        assertThat(definitionsOfSecondModule.size(), is(1));

        validatingForm(definitionsOfFirstModule.get(0), "1_");
        validatingForm(definitionsOfFirstModule.get(1), "2_");
        validatingForm(definitionsOfSecondModule.get(0), "3_");

        assertThat(definitionsOfFirstModule.get(0).extraMappings().get("form|path|to|extra|field"), is("1_ExtraFieldInOutput"));
        assertTrue(definitionsOfFirstModule.get(1).extraMappings().isEmpty());
        assertTrue(definitionsOfSecondModule.get(0).extraMappings().isEmpty());
    }

    private void validatingForm(CommCareFormDefinition formDefinition, String prefix) {
        assertThat(formDefinition.mappings().get("form|path|to|field"), is(prefix + "FieldInOutput"));
        assertThat(formDefinition.mappings().get("form|path|to|another|field"), is(prefix + "AnotherFieldInOutput"));

        assertThat(formDefinition.name(), is(prefix + "Registration"));
        assertThat(formDefinition.url(""), is(prefix + "https://www.commcarehq.org/a/abhilasha/reports/export/?export_tag=%22http://openrosa.org/formdesigner/4FBE07FF-2434-40B3-B151-D2EBE2F4FB4F%22&format=json&previous_export="));
    }
}
