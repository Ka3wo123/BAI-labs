package org.ka3wo.cookieflag;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

public class CookieBuilder {
    private Map<String, String> nameValues;
    private String path;
    private String domain;
    private boolean secure;
    private boolean httpOnly;
    private String sameSite;
    private Integer maxAgeSeconds;
    private String builtCookie;

    public CookieBuilder() {
        this.nameValues = new LinkedHashMap<>();
        this.path = "/";
        this.secure = true;
        this.httpOnly = true;
        this.sameSite = "Strict";
    }



    private static final DateTimeFormatter EXPIRES_FMT =
            DateTimeFormatter.RFC_1123_DATE_TIME.withLocale(Locale.US).withZone(ZoneOffset.UTC);

    public CookieBuilder nameValue(String name, String value) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Cookie name required");
        if (value == null || value.isEmpty()) throw new IllegalArgumentException("Cookie value required");

        this.nameValues.put(name, value);
        return this;
    }

    public CookieBuilder path(String path) {
        this.path = (path == null || path.isEmpty()) ? "/" : path;
        return this;
    }

    public CookieBuilder domain(String domain) {
        this.domain = domain;
        return this;
    }

    public CookieBuilder secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public CookieBuilder httpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public CookieBuilder sameSite(String sameSite) {
        if (sameSite == null) {
            this.sameSite = "Strict";
        } else {
            String s = sameSite.trim();
            if (s.equalsIgnoreCase("Strict") || s.equalsIgnoreCase("Lax") || s.equalsIgnoreCase("None")) {
                this.sameSite = s;
            } else {
                throw new IllegalArgumentException("SameSite must be Strict, Lax or None");
            }
        }
        return this;
    }

    public CookieBuilder maxAgeSeconds(int seconds) {
        if (seconds < 0) throw new IllegalArgumentException("maxAge must be >= 0");
        this.maxAgeSeconds = seconds;
        return this;
    }

    public CookieBuilder build() {
        StringJoiner sj = new StringJoiner("; ");

        for (Map.Entry<String, String> entry : nameValues.entrySet()) {
            sj.add(entry.getKey() + "=" + entry.getValue());
        }

        if (path != null && !path.isEmpty()) sj.add("Path=" + path);
        if (domain != null && !domain.isEmpty()) sj.add("Domain=" + domain);

        if (maxAgeSeconds != null) {
            sj.add("Max-Age=" + maxAgeSeconds);
            Instant expiresAt = Instant.now().plus(Duration.ofSeconds(maxAgeSeconds));
            sj.add("Expires=" + EXPIRES_FMT.format(expiresAt));
        }

        if (secure) sj.add("Secure");
        if (httpOnly) sj.add("HttpOnly");
        if (sameSite != null) sj.add("SameSite=" + sameSite);


        builtCookie = "Set-Cookie: " + sj;
        return this;
    }

    @Override
    public String toString() {
        if (builtCookie == null) {
            throw new IllegalStateException("Cookie not built yet. Call build() first.");
        }
        return builtCookie;
    }
}
