# Podatności aplikacji internetowych

## Secure Cookie Flag
Dodatkowe atrybuty w cookies:
- `Secure` - wysyłane wyłącznie przez HTTPS; zapobiega przechwyceniu przez HTTP
- `HttpOnly` - zapobiega dostępu do cookie przez JavaScript
- `SameSite` - zapobiega CSRF i przed wyciekiem informacji, gdy cookies są przesyłane w requestach między serwerami 

Klasa [Vulnerable_Secure_Cookie_Flag_Nowak_Kajetan](src/main/java/org/ka3wo/cookieflag/Vulnerable_Secure_Cookie_Flag_Nowak_Kajetan.java) 
przedstawia ustawianie id sesji w sposób niebezpieczny (możliwość ustawienia cookies przez atakującego w sposób dowolny)

Klasa [Fixed_Secure_Cookie_Flag_Nowak_Kajetan](src/main/java/org/ka3wo/cookieflag/Fixed_Secure_Cookie_Flag_Nowak_Kajetan.java)
ustawia bezpieczne id sesji domyślnie

### Testy jednostkowe dla Secure Cookie Flag
Wyniki testów są zapisywane w pliku [cookie_results.txt](./test_artifacts/cookie_results.txt). Przykładowe wyniki:
```txt
2025-10-19T08:33:33.388303170Z | fixed.process | payload=ATTACK_STRING_1 | result=Set-Cookie: sessionId=qfYvRuyGQJGlvxJEFGWlLotIEpqPIU7tgpYIZ28eDmc; userId=eedaaeb6-3eca-4688-9ad6-e1c3154f02e8; Path=/; Max-Age=60; Expires=Sun, 19 Oct 2025 08:34:33 GMT; Secure; HttpOnly; SameSite=Lax
2025-10-19T08:33:33.390306230Z | fixed.process | payload=faked-session | result=Set-Cookie: sessionId=G91uNbLe__t6Tvk2DJzYYm5KCIFS_oRvNjHfM-L4-Zs; userId=ad30f1dc-84e2-4f7d-9db1-92aab7242e8c; Path=/; Max-Age=60; Expires=Sun, 19 Oct 2025 08:34:33 GMT; Secure; HttpOnly; SameSite=Lax
2025-10-19T08:33:33.390663569Z | fixed.process | payload=user-input-session | result=Set-Cookie: sessionId=LE8zNYdBn7oQAEC5xPnvGvEuC6NfaGxS_xOIQ6oJf04; userId=35ea6886-f77b-4253-864d-eba6d3f295b1; Path=/; Max-Age=60; Expires=Sun, 19 Oct 2025 08:34:33 GMT; Secure; HttpOnly; SameSite=Lax
2025-10-19T08:33:33.390928690Z | fixed.process | payload=injected_value_123 | result=Set-Cookie: sessionId=7G7t7-3DIQy1K_EE7yd8OF9oHsZ_zxYKGMfEb1wNamY; userId=7da33119-e25d-48da-ab29-ab895a474e8a; Path=/; Max-Age=60; Expires=Sun, 19 Oct 2025 08:34:33 GMT; Secure; HttpOnly; SameSite=Lax
...
```

## XPath Injection

Klasa [Vulnerable_Xpath_Injection_Nowak_Kajetan](src/main/java/org/ka3wo/xpath/Vulnerable_Xpath_Injection_Nowak_Kajetan.java)
przedstawia niebezpieczne używanie zapytania dla XPath `"//user[username/text()='" + username + "']")`.
Atakujący może użyć wyrażenia `' or '1'='1`, które zwróci wszystkich użytkowników z pliku [users.xml](/src/main/resources/users.xml).

Klasa [Fixed_Xpath_Injection_Nowak_Kajetan](src/main/java/org/ka3wo/xpath/Fixed_Xpath_Injection_Nowak_Kajetan.java)
parametryzuje zapytanie XPath `"//user[username/text() = $username]"` tym samym dane są zabezpieczone przed wyciekiem.
Dodatkowo metoda [XMLUtil.loadDocument()](src/main/java/org/ka3wo/xpath/XMLUtil.java) implementuje zabezpieczenia przed XXE 
oraz niechcianym ładowaniem zewnętrznych DTD i plików.

### Testy jednostkowe dla XPath Injection
Wyniki testów są zapisywane w pliku [xpath_results.txt](./test_artifacts/xpath_results.txt). Przykładowe wyniki:
```txt
2025-10-19T08:33:33.439044174Z | fixed.queryByUsername | payload=john | result=match
2025-10-19T08:33:33.442274591Z | fixed.queryByUsername | payload=alice | result=match
2025-10-19T08:33:33.444766445Z | fixed.queryByUsername | payload=bob | result=match
2025-10-19T08:33:33.447003343Z | fixed.queryByUsername | payload=' or '1'='1 | result=match
2025-10-19T08:33:33.449183565Z | fixed.queryByUsername | payload=" or "1"="1 | result=match
2025-10-19T08:33:33.457668515Z | vulnerable.queryByUsername | payload=john | result=match_count=1 matches=<Username: john, password: john123, role: user>
2025-10-19T08:33:33.460433031Z | vulnerable.queryByUsername | payload=alice | result=match_count=1 matches=<Username: alice, password: alice456, role: user>
2025-10-19T08:33:33.462395529Z | vulnerable.queryByUsername | payload=bob | result=no-match
...
```

## Uruchamianie testów
W folderze root należy wywołać komendę `docker-compose run --rm tests`. \
Testy są wykonywane w skonteneryzowanym środowisku Docker, a następnie wyniki zapisywane są w plikach `cookie_result.txt` oraz `xpath_result.txt`.