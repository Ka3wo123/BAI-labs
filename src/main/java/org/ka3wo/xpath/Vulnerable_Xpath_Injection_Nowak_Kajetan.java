package org.ka3wo.xpath;

import java.util.List;

public class Vulnerable_Xpath_Injection_Nowak_Kajetan implements Query {


    @Override
    public List<String> queryByUsername(String username) throws Exception {
        XMLUtil loader = new XMLUtil();
        return loader.extractAll("src/main/resources/users.xml", "//user[username/text()='" + username + "']");
    }
}
