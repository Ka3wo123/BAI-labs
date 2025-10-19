#!/usr/bin/env sh
set -euo pipefail

WORKDIR="/workspace"
ARTIFACT_DIR="${WORKDIR}/test_artifacts"

mkdir -p "${ARTIFACT_DIR}"

chmod +x ./gradlew
./gradlew --no-daemon clean test --stacktrace 2>&1

echo "Test run finished"
