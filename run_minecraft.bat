@echo off
cd test_run
echo Starting Minecraft... > run_log.txt

set JAVA="C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\java.exe"
set M2=C:\Users\Reng\.m2\repository

set CP=../target/classes
set CP=%CP%;%M2%/io/netty/netty-all/4.0.23.Final/netty-all-4.0.23.Final.jar
set CP=%CP%;%M2%/com/mojang/patchy/1.7.7/patchy-1.7.7.jar
set CP=%CP%;%M2%/net/sf/jopt-simple/jopt-simple/4.6/jopt-simple-4.6.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl/3.3.3/lwjgl-3.3.3.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl/3.3.3/lwjgl-3.3.3-natives-windows.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl-glfw/3.3.3/lwjgl-glfw-3.3.3.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl-glfw/3.3.3/lwjgl-glfw-3.3.3-natives-windows.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl-opengl/3.3.3/lwjgl-opengl-3.3.3.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl-opengl/3.3.3/lwjgl-opengl-3.3.3-natives-windows.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl-openal/3.3.3/lwjgl-openal-3.3.3.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl-openal/3.3.3/lwjgl-openal-3.3.3-natives-windows.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl-stb/3.3.3/lwjgl-stb-3.3.3.jar
set CP=%CP%;%M2%/org/lwjgl/lwjgl-stb/3.3.3/lwjgl-stb-3.3.3-natives-windows.jar
set CP=%CP%;%M2%/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar
set CP=%CP%;%M2%/net/java/jinput/jinput-platform/2.0.5/jinput-platform-2.0.5-natives-windows.jar
set CP=%CP%;%M2%/com/mojang/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar
set CP=%CP%;%M2%/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar
set CP=%CP%;%M2%/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar
set CP=%CP%;%M2%/oshi/oshi-core/1.1/oshi-core-1.1.jar
set CP=%CP%;%M2%/net/java/dev/jna/jna/3.4.0/jna-3.4.0.jar
set CP=%CP%;%M2%/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar
set CP=%CP%;%M2%/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar
set CP=%CP%;%M2%/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar
set CP=%CP%;%M2%/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar
set CP=%CP%;%M2%/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar
set CP=%CP%;%M2%/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar
set CP=%CP%;%M2%/tv/twitch/twitch/6.5/twitch-6.5.jar
set CP=%CP%;%M2%/com/google/guava/guava/17.0/guava-17.0.jar
set CP=%CP%;%M2%/org/apache/commons/commons-lang3/3.3.2/commons-lang3-3.3.2.jar
set CP=%CP%;%M2%/commons-io/commons-io/2.4/commons-io-2.4.jar
set CP=%CP%;%M2%/commons-codec/commons-codec/1.9/commons-codec-1.9.jar
set CP=%CP%;%M2%/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar
set CP=%CP%;%M2%/com/mojang/authlib/1.5.21/authlib-1.5.21.jar
set CP=%CP%;%M2%/org/joml/joml/1.10.5/joml-1.10.5.jar

set JAVA_OPTS=--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED

%JAVA% %JAVA_OPTS% -cp "%CP%" net.minecraft.client.main.Main --username TestUser --version MavenMCP --accessToken 0 --assetsDir assets --assetIndex 1.8 --userProperties {} >> run_log.txt 2>&1
echo Exit code: %ERRORLEVEL% >> run_log.txt
