package org.ka3wo.xpath;

import java.util.List;

public class Fixed_Xpath_Injection_Nowak_Kajetan implements Query {

    @Override
    public List<String> queryByUsername(String username) throws Exception {
        String expression = "//user[username/text() = $username]";

        return new XMLUtil().extractAll("src/main/resources/users.xml", expression, username);
    }
}
