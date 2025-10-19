package org.ka3wo.cookieflag;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class Fixed_Secure_Cookie_Flag_Nowak_Kajetan implements HttpRequest {


    @Override
    public String process(String userInput) {


        String sessionId = generateSessionId();
        String userId = UUID.randomUUID().toString();


        return new CookieBuilder()
                .nameValue("sessionId", sessionId)
                .nameValue("userId", userId)
                .secure(true)
                .httpOnly(true)
                .maxAgeSeconds(60)
                .build().toString();
    }

    private String generateSessionId() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64url = Base64.getUrlEncoder().withoutPadding();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return base64url.encodeToString(bytes);
    }
}
