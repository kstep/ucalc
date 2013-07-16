package me.kstep.ucalc.activities;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class me.kstep.ucalc.activities.UCalcActivityTest \
 * me.kstep.ucalc.tests/android.test.InstrumentationTestRunner
 */
public class UCalcActivityTest extends ActivityInstrumentationTestCase2<UCalcActivity> {

    public UCalcActivityTest() {
        super(UCalcActivity.class);
    }

}
