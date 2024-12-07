gradlew -p app assembleRelease && ^
adb -s emulator-5558 push app\build\outputs\apk\release\app-release-unsigned.apk /data/local/tmp/app.jar && ^
adb -s emulator-5558 shell mkdir -p /data/local/tmp/assets && ^
adb -s emulator-5558 push assets\ /data/local/tmp && ^
adb -s emulator-5558 shell CLASSPATH=/data/local/tmp/app.jar app_process / com.bpn.auto.Main