#!/bin/bash
set -euo pipefail

LOG_FILE="$(dirname "$0")/error.log"

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

{
  echo "PROJECT_DIR: $PROJECT_DIR"

  JAVA_FX_HOME=$(find "$PROJECT_DIR" -type d -name "javafx-sdk-11*" | head -n 1)
  JAR_PATH="$PROJECT_DIR/cs2-editor.jar"

  echo "JAVA_FX_HOME: $JAVA_FX_HOME"
  echo "JAR_PATH: $JAR_PATH"

  if [[ ! -d "$JAVA_FX_HOME/lib" ]]; then
    echo "javafx lib folder not found!"
    exit 1
  fi

  if [[ ! -f "$JAR_PATH" ]]; then
    echo "JAR file not found!"
    exit 1
  fi

  java \
    --module-path "$JAVA_FX_HOME/lib" \
    --add-modules javafx.controls,javafx.fxml \
    --add-opens javafx.graphics/javafx.scene.text=ALL-UNNAMED \
    --add-exports javafx.graphics/com.sun.javafx.text=ALL-UNNAMED \
    -jar "$JAR_PATH"

} 2> "$LOG_FILE"
