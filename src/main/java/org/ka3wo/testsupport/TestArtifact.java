package org.ka3wo.testsupport;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

public final class TestArtifact {
  private static final Path ARTIFACT_DIR = Paths.get("test_artifacts");

  public static List<String> readPayloads(String filename) {
    Path payloadFile = ARTIFACT_DIR.resolve(filename);
    try {
      if (Files.exists(payloadFile)) {
        return Files.readAllLines(payloadFile, StandardCharsets.UTF_8);
      } else {
        return Collections.emptyList();
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read payloads file: " + payloadFile, e);
    }
  }

  public static void appendResult(
      String logFilename, String testName, String payload, String result) {
    try {
      Files.createDirectories(ARTIFACT_DIR);
      Path logFile = ARTIFACT_DIR.resolve(logFilename);
      String safePayload =
          payload == null ? "<null>" : payload.replace("\n", "\\n").replace("\r", "\\r");
      String line =
          String.format(
              "%s | %s | payload=%s | result=%s%n",
              Instant.now().toString(), testName, safePayload, result);
      Files.writeString(logFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write test artifact log", e);
    }
  }
}
