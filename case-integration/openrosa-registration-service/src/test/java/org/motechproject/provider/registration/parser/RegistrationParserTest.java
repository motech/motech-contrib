package org.motechproject.provider.registration.parser;

import junit.framework.TestCase;
import org.junit.Test;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationParserException;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/16/12
 * Time: 3:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegistrationParserTest extends TestCase {

    @Test
    public void testshouldParseProviderInfoCorrectly() throws OpenRosaRegistrationParserException {
        RegistrationParser<TestProvider> parser = new RegistrationParser<TestProvider>(TestProvider.class, xmlDocument());
        TestProvider testProvider = parser.parseProvider();
        assertEquals("984657764", testProvider.getPrimary_mobile());
        assertEquals("984658864", testProvider.getSecondary_mobile());
        assertEquals("984659964", testProvider.getTertiary_mobile());
        assertEquals("cha011", testProvider.getProvider_id());
        assertEquals("Muzaffarpur", testProvider.getDistrict());

    }

    private String xmlDocument() {
        return "<Registration xmlns=\"http://openrosa.org/user/registration\">\n" +
                "\n" +
                "    <username></username>\n" +
                "    <password></password>\n" +
                "    <uuid></uuid>\n" +
                "    <date></date>\n" +
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
