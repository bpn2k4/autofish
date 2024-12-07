gradlew -p app assembleRelease && ^
adb push app\build\outputs\apk\release\app-release-unsigned.apk /data/local/tmp/app.jar && ^
adb shell mkdir -p /data/local/tmp/assets && ^
adb push assets\ /data/local/tmp && ^
adb shell CLASSPATH=/data/local/tmp/app.jar app_process / com.bpn.auto.Test