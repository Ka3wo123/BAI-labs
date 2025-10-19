package org.ka3wo.xpath;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Fixed_Xpath_Injection_Nowak_KajetanTest {
    @Test
    public void shouldReturnOnlyRequestedUser() throws Exception {
        Fixed_Xpath_Injection_Nowak_Kajetan fixed = new Fixed_Xpath_Injection_Nowak_Kajetan();
        final String USERNAME = "john";
        final String EXPECTED = "<Username: john, password: john123, role: user>";

        List<String> result = fixed.queryByUsername(USERNAME);

        assertEquals(1, result.size());
        assertEquals(EXPECTED, result.get(0));
    }

    @Test
    public void shouldReturnEmptyListWhenXPathInject() throws Exception{
        Fixed_Xpath_Injection_Nowak_Kajetan fixed = new Fixed_Xpath_Injection_Nowak_Kajetan();
        final String XPATH_INJECTION = "' or '1'='1";

        List<String> result = fixed.queryByUsername(XPATH_INJECTION);

        assertEquals(0, result.size());
    }

}
