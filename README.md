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
2025-10-19T08:45:45.117195736Z | fixed.process | payload="double-quote" | result=Set-Cookie: sessionId=PeecxTYaAVc8DcyL21l6-sCh8kjHmsNxNwOxKu0pd20; userId=f2928b69-8133-41e4-afcb-cd1fc4071017; Path=/; Max-Age=60; Expires=Sun, 19 Oct 2025 08:46:45 GMT; Secure; HttpOnly; SameSite=Strict
2025-10-19T08:46:39.736582513Z | fixed.process | payload=ATTACK_STRING_1 | result=Set-Cookie: sessionId=EyfKz__NPLzg97rgdcTg8EnpORWw94NR_o1LfKmqQxA; userId=29778fe8-6dbd-4a2f-a610-b2cd95d6f4c6; Path=/; Max-Age=60; Expires=Sun, 19 Oct 2025 08:47:39 GMT; Secure; HttpOnly; SameSite=Strict
2025-10-19T08:46:39.739453807Z | fixed.process | payload="double-quote" | result=Set-Cookie: sessionId=FXQQJ9UqHzw2ZyP-8fPtYgcvnINDSRd7yJYrfAi6RUo; userId=e6acc598-d80c-4647-b5e9-93d221005652; Path=/; Max-Age=60; Expires=Sun, 19 Oct 2025 08:47:39 GMT; Secure; HttpOnly; SameSite=Strict
2025-10-19T09:03:11.964287189Z | vulnerable.process | payload=ATTACK_STRING_1 | result=Set-Cookie: sessionId=ATTACK_STRING_1
2025-10-19T09:03:11.967787443Z | vulnerable.process | payload=injected_value_123 | result=Set-Cookie: sessionId=injected_value_123
2025-10-19T09:03:11.967897939Z | vulnerable.process | payload="double-quote" | result=Set-Cookie: sessionId="double-quote"
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