package org.ka3wo.cookieflag;

import org.junit.jupiter.api.Test;
import org.ka3wo.testsupport.TestArtifact;

import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class CookiePayloadsTest {
    private static final String PAYLOAD_FILE = "payloads_cookie.txt";
    private static final String LOG_FILE = "cookie_results.txt";
    private static final Pattern BASE64URL_NO_PADDING = Pattern.compile("^[A-Za-z0-9_-]{43}$");

    @Test
    void shouldCreateSecuredCookie() {
        Fixed_Secure_Cookie_Flag_Nowak_Kajetan fixed = new Fixed_Secure_Cookie_Flag_Nowak_Kajetan();
        List<String> payloads = TestArtifact.readPayloads(PAYLOAD_FILE);

        for (String raw : payloads) {
            if (raw == null) continue;
            String p = raw.trim();
            if (p.isEmpty() || p.startsWith("#")) continue;

            String cookie = fixed.process(p);
            assertNotNull(cookie);
            TestArtifact.appendResult(LOG_FILE, "fixed.process", p, cookie);

            String token = extractCookieValue(cookie, "sessionId");
            assertNotNull(token, "sessionId absent for payload: " + p);
            assertNotEquals(p, token, "sessionId must not be attacker-supplied payload: " + p);
            assertTrue(cookie.contains("Secure"));
            assertTrue(cookie.contains("HttpOnly"));
            assertTrue(cookie.contains("SameSite=Strict"));

            byte[] decoded = null;
            try {
                decoded = Base64.getUrlDecoder().decode(token);
            } catch (IllegalArgumentException ex) {
                fail("sessionId token is not valid Base64URL: " + token);
            }
            assertEquals(32, decoded.length, "sessionId decoded length must be 32 bytes");

            assertTrue(BASE64URL_NO_PADDING.matcher(token).matches(), "sessionId should match base64url no-padding pattern");
        }
    }

    private static String extractCookieValue(String setCookieHeader, String name) {
        if (setCookieHeader == null) return null;
        String s = setCookieHeader.startsWith("Set-Cookie: ") ?
                setCookieHeader.substring("Set-Cookie: ".length()) : setCookieHeader;
        String[] parts = s.split(";");
        for (String pr : parts) {
            String pair = pr.trim();
            if (pair.startsWith(name + "=")) {
                return pair.substring((name + "=").length());
            }
        }
        return null;
    }
}
