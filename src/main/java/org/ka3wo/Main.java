package org.ka3wo;

import org.ka3wo.cookieflag.Fixed_Secure_Cookie_Flag_Nowak_Kajetan;
import org.ka3wo.cookieflag.Vulnerable_Secure_Cookie_Flag_Nowak_Kajetan;
import org.ka3wo.xpath.Fixed_Xpath_Injection_Nowak_Kajetan;
import org.ka3wo.xpath.Vulnerable_Xpath_Injection_Nowak_Kajetan;

import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    final String ATTACK_INPUT = "' or '1'='1";
    final String USERNAME_QUERY = "alice";

    Vulnerable_Secure_Cookie_Flag_Nowak_Kajetan vscf =
        new Vulnerable_Secure_Cookie_Flag_Nowak_Kajetan();
    String ui1 = scanner.next();
    String r1 = vscf.process(ui1);
    System.out.println(r1);

    Fixed_Secure_Cookie_Flag_Nowak_Kajetan fscf = new Fixed_Secure_Cookie_Flag_Nowak_Kajetan();
    String ui2 = scanner.next();
    String r2 = fscf.process(ui2);
    System.out.println(r2);

    Vulnerable_Xpath_Injection_Nowak_Kajetan vxi = new Vulnerable_Xpath_Injection_Nowak_Kajetan();
    List<String> vulXpathResult = vxi.queryByUsername(ATTACK_INPUT);
    System.out.println(vulXpathResult);

    Fixed_Xpath_Injection_Nowak_Kajetan sxi = new Fixed_Xpath_Injection_Nowak_Kajetan();
    List<String> securedXpathResult = sxi.queryByUsername(ATTACK_INPUT);
    System.out.println(securedXpathResult);
    List<String> securedXpathResult2 = sxi.queryByUsername(USERNAME_QUERY);
    System.out.println(securedXpathResult2);
  }
}
