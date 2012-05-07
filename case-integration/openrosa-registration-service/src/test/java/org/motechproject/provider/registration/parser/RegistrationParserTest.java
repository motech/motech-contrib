package org.motechproject.provider.registration.parser;

import org.junit.Test;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationParserException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RegistrationParserTest {

    @Test
    public void shouldParseProviderInfoCorrectly() throws OpenRosaRegistrationParserException {
        RegistrationParser<TestProvider> parser = new RegistrationParser<TestProvider>(TestProvider.class, xmlDocument());
        TestProvider testProvider = parser.parseProvider();
        assertEquals("984657764", testProvider.getPrimary_mobile());
        assertEquals("984658864", testProvider.getSecondary_mobile());
        assertEquals("984659964", testProvider.getTertiary_mobile());
        assertEquals("cha011", testProvider.getProvider_id());
        assertEquals("Muzaffarpur", testProvider.getDistrict());
        assertEquals("Name", testProvider.getUsername());
        assertEquals("pwd", testProvider.getPassword());
        assertEquals("123456", testProvider.getUuid());
        assertEquals("19/02/2012", testProvider.getDate());

    }

    @Test
    public void shouldFetchAPIKeyFromProviderXML() throws OpenRosaRegistrationParserException {
        RegistrationParser<TestProvider> parser = new RegistrationParser<TestProvider>(TestProvider.class, xmlDocument());
        TestProvider testProvider = parser.parseProvider();
        assertEquals("API_KEY", testProvider.getApi_key());
    }

    @Test
    public void shouldNotThrowExceptionWhenAPIKeyNotPresentInXML() throws OpenRosaRegistrationParserException {
        RegistrationParser<TestProvider> parser = new RegistrationParser<TestProvider>(TestProvider.class, xmlDocumentWithoutAPIKey());
        TestProvider testProvider = parser.parseProvider();
        assertEquals("", testProvider.getApi_key());
    }

    private String xmlDocument() {
        return "<Registration api_key=\"API_KEY\" xmlns=\"http://openrosa.org/user/registration\">\n" +
                "\n" +
                "    <username>Name</username>\n" +
                "    <password>pwd</password>\n" +
                "    <uuid>123456</uuid>\n" +
                "    <date>19/02/2012</date>\n" +
                "\n" +
                "    <registering_phone_id></registering_phone_id>\n" +
                "\n" +
                "    <user_data>\n" +
                "        <data key=\"primary_mobile\">984657764</data>\n" +
                "        <data key=\"secondary_mobile\">984658864</data>\n" +
                "        <data key=\"tertiary_mobile\">984659964</data>\n" +
                "        <data key=\"provider_id\">cha011</data>\n" +
                "        <data key=\"district\">Muzaffarpur</data>\n" +
                "    </user_data>\n" +
                "</Registration>\n" +
                "";  //To change body of created methods use File | Settings | File Templates.
    }

    private String xmlDocumentWithoutAPIKey() {
        return "<Registration xmlns=\"http://openrosa.org/user/registration\">\n" +
                "\n" +
                "    <username>Name</username>\n" +
                "    <password>pwd</password>\n" +
                "    <uuid>123456</uuid>\n" +
                "    <date>19/02/2012</date>\n" +
                "\n" +
                "    <registering_phone_id></registering_phone_id>\n" +
                "\n" +
                "    <user_data>\n" +
                "        <data key=\"primary_mobile\">984657764</data>\n" +
                "        <data key=\"secondary_mobile\">984658864</data>\n" +
                "        <data key=\"tertiary_mobile\">984659964</data>\n" +
                "        <data key=\"provider_id\">cha011</data>\n" +
                "        <data key=\"district\">Muzaffarpur</data>\n" +
                "    </user_data>\n" +
                "</Registration>\n" +
                "";  //To change body of created methods use File | Settings | File Templates.
    }
}
