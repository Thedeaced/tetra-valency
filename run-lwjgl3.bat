@echo off
setlocal
cd /d "%~dp0"
call .\gradlew.bat --stop
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM javaw.exe >nul 2>&1
powershell -NoProfile -Command "Remove-Item -Recurse -Force \"$env:USERPROFILE\\.gradle\\caches\\modules-2\\files-2.1\\org.lwjgl\\lwjgl\" -ErrorAction SilentlyContinue"
call .\gradlew.bat --refresh-dependencies :lwjgl3:run
endlocal
