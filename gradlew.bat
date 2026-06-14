@echo off
where gradle >nul 2>nul
if %errorlevel%==0 (
  gradle %*
) else (
  echo Gradle command not found. Build this on Codemagic or Android Studio.
  exit /b 1
)
