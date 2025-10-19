package org.ka3wo.cookieflag;

public class Vulnerable_Secure_Cookie_Flag_Nowak_Kajetan implements HttpRequest {
    @Override
    public String process(String userInput) {
        String sessionId = userInput;

        return "Set-Cookie: sessionId=" + sessionId;
    }
}
