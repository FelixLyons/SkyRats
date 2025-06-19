:: Script for building both versions of the mod, need to change the file directories to point to Java 8 and 21 JDKs

@echo off

REM ---- Build 1.8.9 ----
set "JAVA_HOME=C:\Program Files\Java\zulu8.86.0.25-ca-jdk8.0.452-win_x64"
set "Path=%JAVA_HOME%\bin;%Path%"
cd /d "%~dp0\1.8.9"
echo Building 1.8.9 with:
java -version
call gradlew.bat clean build

REM ---- Build 1.21.5 ----
set "JAVA_HOME=C:\Users\felix\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
set "Path=%JAVA_HOME%\bin;%Path%"
cd /d "%~dp0\1.21.5"
echo Building 1.21.5 with:
java -version
call gradlew.bat clean build

pause