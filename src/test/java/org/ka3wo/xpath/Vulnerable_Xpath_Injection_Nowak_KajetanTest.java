package org.ka3wo.xpath;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Vulnerable_Xpath_Injection_Nowak_KajetanTest {
    @Test
    public void shouldReturnAllUsers() throws Exception {
        Vulnerable_Xpath_Injection_Nowak_Kajetan vul = new Vulnerable_Xpath_Injection_Nowak_Kajetan();
        final String XPATH_INJECTION = "' or '1'='1";

        List<String> result = vul.queryByUsername(XPATH_INJECTION);

        assertEquals(3, result.size());
        assertEquals("<Username: admin, password: secret_admin_pass, role: administrator>", result.get(0));
        assertEquals("<Username: alice, password: alice456, role: user>", result.get(2));
    }
}
