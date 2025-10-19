FROM openjdk:21-jdk-slim
WORKDIR /workspace
COPY gradlew gradlew.bat gradle/ settings.gradle /workspace/
COPY gradle /workspace/gradle
COPY src /workspace/src
RUN chmod +x gradlew
ENV ARTIFACT_DIR=/workspace/test_artifacts
CMD ["./gradlew", "test"]
