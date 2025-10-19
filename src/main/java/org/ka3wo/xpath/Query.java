package org.ka3wo.xpath;

import java.util.List;

public interface Query {
    List<String> queryByUsername(String username) throws Exception;
}
