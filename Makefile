
JAVA_PROJECT_CLASSPATH=src:gen
JAVA_CLASSPATH=$(JAVA_PROJECT_CLASSPATH):libs/android-support-v4.jar:/opt/android-sdk/platforms/android-15/android.jar:/opt/android-sdk/platforms/android-15/data/layoutlib.jar
JAVA_TEST_CLASSPATH=$(JAVA_PROJECT_CLASSPATH):tests/src:/usr/share/java/junit.jar:/usr/share/java/robolectric-1.2.jar:tests/libs/xpp3-1.1.4c.jar:/opt/android-sdk/platforms/android-15/android.jar

JAVA_SRC=src/me/kstep/ucalc/numbers/*.java src/me/kstep/ucalc/operations/*.java src/me/kstep/ucalc/units/*.java src/me/kstep/ucalc/views/*.java  src/me/kstep/ucalc/widgets/*.java src/me/kstep/ucalc/activities/*.java src/me/kstep/ucalc/collections/*.java src/me/kstep/ucalc/preferences/*.java
IMG_RES=res/drawable/*.xml res/drawable/*.png res/drawable-hdpi/*.png res/drawable-ldpi/*.png res/drawable-mdpi/*.png res/drawable-xhdpi/*.png
XML_RES=res/xml/*.xml res/layout/*.xml res/values/*.xml res/color/*.xml res/menu/*.xml res/values-ru/*.xml
ASSETS_RES=assets/fonts/*.ttf
MANIFEST=AndroidManifest.xml

ALL=$(JAVA_SRC) $(IMG_RES) $(XML_RES) $(ASSETS_RES) $(MANIFEST)

DEBUG_APK=bin/UCalc-debug.apk
UNSIGNED_APK=bin/UCalc-release-unsigned.apk
SIGNED_APK=bin/UCalc-release-signed.apk
RELEASE_APK=bin/UCalc-release.apk

compile:
	find src -name \*.java -exec javac -cp $(JAVA_CLASSPATH) {} +
	find tests/src -name \*.java -exec javac -cp $(JAVA_TEST_CLASSPATH) {} +

test:
	java -cp $(JAVA_PROJECT_CLASSPATH):tests/src:/usr/share/java/junit.jar:/usr/share/java/robolectric-1.2.jar:tests/libs/xpp3-1.1.4c.jar org.junit.runner.JUnitCore me.kstep.ucalc.units.ConversionTest
	#java -cp $(JAVA_PROJECT_CLASSPATH):tests/src:/usr/share/java/junit.jar:/usr/share/java/robolectric-1.2.jar:tests/libs/xpp3-1.1.4c.jar org.junit.runner.JUnitCore me.kstep.ucalc.units.UnitNetworkLoadTest

# Debug version
$(DEBUG_APK): $(ALL)
	ant debug

# Unsigned release version
$(UNSIGNED_APK): $(ALL)
	ant release

# Signed version is made from unsigned one
$(SIGNED_APK): $(UNSIGNED_APK)
	jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore my-release-key.keystore -signedjar $(SIGNED_APK) $(UNSIGNED_APK) release-key

# Then we do release version out of signed one by aligning
$(RELEASE_APK): $(SIGNED_APK)
	zipalign -v 4 $(SIGNED_APK) $(RELEASE_APK)


# Aliases for convinience

# make debug apk
debug: $(DEBUG_APK)

# make release apk
release: $(RELEASE_APK)

# make signed apk
sign: $(SIGNED_APK)

# make aligned apk (an alias for release)
align: $(RELEASE_APK)

# Cleanup
clean:
	find . -name \*.class -delete
	ant clean

# Different installation types
installd: debug
	ant installd

installr: release
	ant installr


# Utility targets

verify:
	jarsigner -verify -verbose -certs $(RELEASE_APK)

log:
	adb logcat | grep E/AndroidRuntime

start:
	adb shell am start -n me.kstep.ucalc/me.kstep.ucalc.activities.UCalcActivity

uninstall:
	adb uninstall me.kstep.ucalc

$(MANIFEST): $(MANIFEST).in
	sed -e "s/android:versionName=\"VERSION\"/android:versionName=\"$$(git describe --tags | sed 's/^v//')\"/" < $(MANIFEST).in > $(MANIFEST)

.PHONY: clean debug installd uninstall log start sign align release verify installr update-version AndroidManifest.xml
