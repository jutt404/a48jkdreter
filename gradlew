#!/usr/bin/env sh
# Lightweight Gradle launcher for Codemagic and Android cloud builders.
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi
echo "Gradle command not found. Build this on Codemagic or open the project in Android Studio." >&2
exit 1
