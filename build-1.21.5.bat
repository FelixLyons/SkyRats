:: Script to build 1.21.5, need to change the file directory to point to Java 21 JDK

@echo off
REM Set JAVA_HOME to Java 21 for Fabric 1.21.5 build
set "JAVA_HOME=C:\Users\felix\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
set "Path=%JAVA_HOME%\bin;%Path%"

cd /d "%~dp0\1.21.5"
echo Using Java version:
java -version

call gradlew.bat clean build

pause