package org.ormfux.common.utils.testdateutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.ormfux.common.datatype.TimeUnit;
import org.ormfux.common.utils.DateUtils;

public class ShiftTest {
    
    @Test
    public void testShiftDay() {
        assertEquals(DateUtils.getDate(2000, 1, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), 0, TimeUnit.DAY));
        assertEquals(DateUtils.getDate(2000, 1, 2), DateUtils.shift(getDateWithTime(2000, 1, 1), 1, TimeUnit.DAY));
        assertEquals(DateUtils.getDate(2000, 1, 31), DateUtils.shift(getDateWithTime(2000, 1, 1), 30, TimeUnit.DAY));
        assertEquals(DateUtils.getDate(2000, 2, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), 31, TimeUnit.DAY));
        
        assertEquals(DateUtils.getDate(1999, 12, 31), DateUtils.shift(getDateWithTime(2000, 1, 1), -1, TimeUnit.DAY));
        assertEquals(DateUtils.getDate(1999, 12, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), -31, TimeUnit.DAY));
        assertEquals(DateUtils.getDate(1999, 11, 30), DateUtils.shift(getDateWithTime(2000, 1, 1), -32, TimeUnit.DAY));
        
        //leap year
        assertEquals(DateUtils.getDate(2000, 2, 29), DateUtils.shift(getDateWithTime(2000, 2, 28), 1, TimeUnit.DAY));
        //non-leap year
        assertEquals(DateUtils.getDate(2001, 3, 1), DateUtils.shift(getDateWithTime(2001, 2, 28), 1, TimeUnit.DAY));
        
    }
    
    @Test
    public void testShiftMonth() {
        for (int shiftVal = 0; shiftVal < 12; shiftVal++) {
            assertEquals(DateUtils.getDate(2000, 1 + shiftVal, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), shiftVal, TimeUnit.MONTH));
        }
        
        assertEquals(DateUtils.getDate(2001, 1, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), 12, TimeUnit.MONTH));
        
        for (int shiftVal = 1; shiftVal < 12; shiftVal++) {
            assertEquals(DateUtils.getDate(1999, 13 - shiftVal, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), -shiftVal, TimeUnit.MONTH));
        }
        
        //end of month, leap year
        assertEquals(DateUtils.getDate(2000, 2, 29), DateUtils.shift(getDateWithTime(2000, 1, 31), 1, TimeUnit.MONTH));
        assertEquals(DateUtils.getDate(2001, 2, 28), DateUtils.shift(getDateWithTime(2001, 1, 31), 1, TimeUnit.MONTH));
        assertEquals(DateUtils.getDate(2000, 1, 29), DateUtils.shift(getDateWithTime(2000, 2, 29), -1, TimeUnit.MONTH));
        assertEquals(DateUtils.getDate(2000, 1, 28), DateUtils.shift(getDateWithTime(2000, 2, 28), -1, TimeUnit.MONTH));
    }
    
    @Test
    public void testShiftYear() {
        assertEquals(DateUtils.getDate(2000, 1, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), 0, TimeUnit.YEAR));
        assertEquals(DateUtils.getDate(2001, 1, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), 1, TimeUnit.YEAR));
        assertEquals(DateUtils.getDate(2005, 1, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), 5, TimeUnit.YEAR));
        assertEquals(DateUtils.getDate(1999, 1, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), -1, TimeUnit.YEAR));
        assertEquals(DateUtils.getDate(1995, 1, 1), DateUtils.shift(getDateWithTime(2000, 1, 1), -5, TimeUnit.YEAR));
        
        //leap year
        assertEquals(DateUtils.getDate(2001, 2, 28), DateUtils.shift(getDateWithTime(2000, 2, 29), 1, TimeUnit.YEAR));
        assertEquals(DateUtils.getDate(2000, 3, 1), DateUtils.shift(getDateWithTime(2001, 3, 1), -1, TimeUnit.YEAR));
    }
    
    @Test
    public void testShiftNowDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date shiftedDate = DateUtils.shift(1, TimeUnit.DAY);
        assertTrue(DateUtils.isSameDay(calendar.getTime(), shiftedDate));
        
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        shiftedDate = DateUtils.shift(-1, TimeUnit.DAY);
        assertTrue(DateUtils.isSameDay(calendar.getTime(), shiftedDate));
        
    }
    
    @Test
    public void testShiftNowMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Date shiftedDate = DateUtils.shift(1, TimeUnit.MONTH);
        assertTrue(DateUtils.isSameDay(calendar.getTime(), shiftedDate));
        
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        shiftedDate = DateUtils.shift(-1, TimeUnit.MONTH);
        assertTrue(DateUtils.isSameDay(calendar.getTime(), shiftedDate));
        
    }
    
    @Test
    public void testShiftNowYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date shiftedDate = DateUtils.shift(1, TimeUnit.YEAR);
        assertTrue(DateUtils.isSameDay(calendar.getTime(), shiftedDate));
        
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        shiftedDate = DateUtils.shift(-1, TimeUnit.YEAR);
        assertTrue(DateUtils.isSameDay(calendar.getTime(), shiftedDate));
        
    }
    
    @Test(expected = NullPointerException.class)
    public void testMissingTimeUnit() {
        DateUtils.shift(getDateWithTime(2000, 3, 3), 1, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNowMissingTimeUnit() {
        DateUtils.shift(1, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testMissingDate() {
        DateUtils.shift(null, 1, TimeUnit.YEAR);
    }
    
    private Date getDateWithTime(final int year, final int month, final int day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        
        return calendar.getTime();
        
    }
}
