package org.ka3wo.cookieflag;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Fixed_Secure_Cookie_Flag_Test {

  @Test
  public void shouldContainMinimalSecureFlags() {
    CookieBuilder cb = new CookieBuilder().nameValue("sessionId", "01245").build();

    String cookie = cb.toString();
    assertNotNull(cookie);
    assertTrue(cookie.startsWith("Set-Cookie: "));
    assertTrue(cookie.contains("sessionId"));
    assertTrue(cookie.contains("Secure"));
    assertTrue(cookie.contains("HttpOnly"));
    assertTrue(cookie.contains("SameSite=Strict"));
  }

  @Test
  public void shouldCreateSecureCookieWithSpecifiedValues() {
    Fixed_Secure_Cookie_Flag_Nowak_Kajetan fixed = new Fixed_Secure_Cookie_Flag_Nowak_Kajetan();
    final String ATTACK_SESSION_ID = "f:dkr-0034^^5lk";

    String cookie = fixed.process(ATTACK_SESSION_ID);

    assertTrue(cookie.startsWith("Set-Cookie: "));
    assertFalse(cookie.contains("sessionId: " + ATTACK_SESSION_ID));
    assertTrue(cookie.contains("Secure"));
    assertTrue(cookie.contains("HttpOnly"));
    assertTrue(cookie.contains("SameSite=Lax"));
    assertTrue(cookie.contains("Max-Age=60"));
    assertTrue(cookie.contains("Path=/"));
  }
}
