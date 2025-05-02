gradlew -p app assembleRelease && ^
adb push app\build\outputs\apk\release\app-release-unsigned.apk /data/local/tmp/app.jar && ^
adb shell CLASSPATH=/data/local/tmp/app.jar app_process / com.bpn.auto.Main