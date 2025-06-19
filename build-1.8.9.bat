:: Script to build 1.8.9, need to change the file directory to point to Java 8 JDK

@echo off
REM Set JAVA_HOME to Java 8 for Forge 1.8.9 build
set "JAVA_HOME=C:\Program Files\Java\zulu8.86.0.25-ca-jdk8.0.452-win_x64"
set "Path=%JAVA_HOME%\bin;%Path%"

cd /d "%~dp0\1.8.9"
echo Using Java version:
java -version

call gradlew.bat clean build

pause