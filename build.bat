@echo off
git submodule update --init --recursive
.\gradlew setupCIWorkspace build
pause
