package org.ka3wo.cookieflag;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vulnerable_Secure_Cookie_Flag_Nowak_KajetanTest {

  @Test
  public void shouldCreateCookieWithNoSecured() {
    Vulnerable_Secure_Cookie_Flag_Nowak_Kajetan vul =
        new Vulnerable_Secure_Cookie_Flag_Nowak_Kajetan();
    final String ATTACK_SESSION_ID = "f:dkr-0034^^5lk";

    String cookie = vul.process(ATTACK_SESSION_ID);

    assertTrue(cookie.startsWith("Set-Cookie: "));
    assertTrue(cookie.contains("sessionId=" + ATTACK_SESSION_ID));
    assertFalse(cookie.contains("Secure"));
    assertFalse(cookie.contains("HttpOnly"));
    assertFalse(cookie.contains("SameSite=Strict"));
  }
}
