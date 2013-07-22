package br.com.androidzin.pontopro.test.settings;

import junit.framework.TestCase;

import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import br.com.androidzin.pontopro.util.Constants;

public class BusinessHourTest extends TestCase{

    public void testEnteredCheckinValidator(){

        final String enteredCheckinKey = BusinessHourCommom.ENTERED_CHECKIN_KEY;
        assertEquals(false,
                BusinessHourCommom.isNewCheckinValid(enteredCheckinKey,
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis * 10),
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis * 11)));

        assertEquals(false,
                BusinessHourCommom.isNewCheckinValid(enteredCheckinKey,
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis*12),
                        Long.valueOf(Constants.hoursInMilis*10),
                        Long.valueOf(Constants.hoursInMilis*11)));

        assertEquals(true,
                BusinessHourCommom.isNewCheckinValid(enteredCheckinKey,
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis*10),
                        Long.valueOf(Constants.hoursInMilis*12),
                        Long.valueOf(Constants.hoursInMilis)));
    }

    public void testLunchCheckinValidator(){

        final String lunchCheckinKey = BusinessHourCommom.LUNCH_CHECKIN_KEY;
        assertEquals(false,
                BusinessHourCommom.isNewCheckinValid(lunchCheckinKey,
                        Long.valueOf(Constants.hoursInMilis*10),
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis*11),
                        Long.valueOf(Constants.hoursInMilis*12)));

        assertEquals(false,
                BusinessHourCommom.isNewCheckinValid(lunchCheckinKey,
                Long.valueOf(Constants.hoursInMilis*14),
                Long.MAX_VALUE,
                Long.valueOf(Constants.hoursInMilis*16),
                Long.valueOf(Constants.hoursInMilis*12)));

        assertEquals(true,
                BusinessHourCommom.isNewCheckinValid(lunchCheckinKey,
                        Long.valueOf(Constants.hoursInMilis*10),
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis*12),
                        Long.valueOf(Constants.hoursInMilis*11)));
    }

    public void testLeavingCheckinValidator(){

        final String leavingCheckinKey = BusinessHourCommom.LEAVING_CHECKIN_KEY;
        assertEquals(false,
                BusinessHourCommom.isNewCheckinValid(leavingCheckinKey,
                        Long.valueOf(Constants.hoursInMilis*10),
                        Long.valueOf(Constants.hoursInMilis*16),
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis*12)));

        assertEquals(false,
                BusinessHourCommom.isNewCheckinValid(leavingCheckinKey,
                        Long.valueOf(Constants.hoursInMilis*16),
                        Long.valueOf(Constants.hoursInMilis*13),
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis*12)));

        assertEquals(true,
                BusinessHourCommom.isNewCheckinValid(leavingCheckinKey,
                        Long.valueOf(Constants.hoursInMilis*10),
                        Long.valueOf(Constants.hoursInMilis*11),
                        Long.MAX_VALUE,
                        Long.valueOf(Constants.hoursInMilis*12)));


    }

    public void testHasWorkingTimeViolation(){
        final Long enteredCheckin = Long.valueOf(Constants.hoursInMilis * 8);
        final Long leavingCheckin = Long.valueOf(Constants.hoursInMilis * 17);
        final Long workingTime = Long.valueOf(Constants.hoursInMilis * 8);
        final Long eatingInterval1 = Long.valueOf(Constants.hoursInMilis);
        final Long eatingInterval2 = Long.valueOf(Constants.hoursInMilis*2);

        assertEquals(true,
                BusinessHourCommom.hasWorkingTimeViolation(enteredCheckin,
                        leavingCheckin,
                        workingTime,
                        eatingInterval1));

        assertEquals(false,
                BusinessHourCommom.hasWorkingTimeViolation(enteredCheckin,
                        leavingCheckin,
                        workingTime,
                        eatingInterval2));
    }

}
