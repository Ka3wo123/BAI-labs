package org.ka3wo.xpath;

import org.junit.jupiter.api.Test;
import org.ka3wo.testsupport.TestArtifact;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class XPathPayloadsTest {
  private static final String PAYLOAD_FILE = "payloads_xpath_injection.txt";
  private static final String LOG_FILE = "xpath_results.txt";

  @Test
  void fixedHandlesXpathPayloads() {
    Fixed_Xpath_Injection_Nowak_Kajetan fixed = new Fixed_Xpath_Injection_Nowak_Kajetan();
    List<String> payloads = TestArtifact.readPayloads(PAYLOAD_FILE);

    for (String raw : payloads) {
      if (raw == null) continue;
      String p = raw.trim();
      if (p.isEmpty() || p.startsWith("#")) continue;

      try {
        List<String> res = fixed.queryByUsername(p);
        TestArtifact.appendResult(
            LOG_FILE, "fixed.queryByUsername", p, res == null ? "no-match" : "match");
      } catch (Exception ex) {
        TestArtifact.appendResult(
            LOG_FILE, "fixed.queryByUsername", p, "exception: " + ex.getMessage());
        fail("Fixed implementation threw for payload: " + p + " -> " + ex.getMessage());
      }
    }
  }

  @Test
  void vulnerableRecordsBehaviorForXpathPayloads() {
    Vulnerable_Xpath_Injection_Nowak_Kajetan vulnerable =
        new Vulnerable_Xpath_Injection_Nowak_Kajetan();
    List<String> payloads = TestArtifact.readPayloads(PAYLOAD_FILE);

    for (String raw : payloads) {
      if (raw == null) continue;
      String p = raw.trim();
      if (p.isEmpty() || p.startsWith("#")) continue;

      try {
        List<String> resList = vulnerable.queryByUsername(p);
        String resSummary;
        if (resList == null || resList.isEmpty()) {
          resSummary = "no-match";
        } else {
          resSummary = "match_count=" + resList.size() + " matches=" + String.join(" | ", resList);
        }
        TestArtifact.appendResult(LOG_FILE, "vulnerable.queryByUsername", p, resSummary);
      } catch (Exception ex) {
        TestArtifact.appendResult(
            LOG_FILE,
            "vulnerable.queryByUsername",
            p,
            "exception: " + ex.getClass().getSimpleName() + ":" + ex.getMessage());
        if (!p.contains("'")) {
          fail("Vulnerable implementation threw unexpectedly for payload without quote: " + p);
        }
      }
    }
  }
}
