package org.ormfux.common.utils.testdateutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.ormfux.common.utils.DateUtils;
import org.junit.Test;

public class IsAfterDayTest {
    
    @Test
    public void sameDay() {
        Calendar calendar1 = DateUtils.getCalendar(null, null);
        Calendar calendar2 = DateUtils.getCalendar(null, null);
        assertFalse(DateUtils.isAfterDay(calendar1.getTime(), calendar2.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 1);
        assertFalse(DateUtils.isAfterDay(calendar1.getTime(), calendar2.getTime()));
        assertFalse(DateUtils.isAfterDay(calendar2.getTime(), calendar1.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 999);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        assertFalse(DateUtils.isAfterDay(calendar1.getTime(), calendar2.getTime()));
        assertFalse(DateUtils.isAfterDay(calendar2.getTime(), calendar1.getTime()));
        
    }
    
    @Test
    public void beforeDay() {
        Calendar calendar1 = DateUtils.getCalendar(DateUtils.getDate(2000, 1, 1), null);
        Calendar calendar2 = DateUtils.getCalendar(DateUtils.getDate(2000, 1, 2), null);
        assertFalse(DateUtils.isAfterDay(calendar1.getTime(), calendar2.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 1);
        assertFalse(DateUtils.isAfterDay(calendar1.getTime(), calendar2.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 999);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        assertFalse(DateUtils.isAfterDay(calendar1.getTime(), calendar2.getTime()));
    }
    
    @Test
    public void afterDay() {
        Calendar calendar1 = DateUtils.getCalendar(DateUtils.getDate(2000, 1, 1), null);
        Calendar calendar2 = DateUtils.getCalendar(DateUtils.getDate(2000, 1, 2), null);
        assertTrue(DateUtils.isAfterDay(calendar2.getTime(), calendar1.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 1);
        assertTrue(DateUtils.isAfterDay(calendar2.getTime(), calendar1.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 999);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        assertTrue(DateUtils.isAfterDay(calendar2.getTime(), calendar1.getTime()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingDate1() {
        DateUtils.isAfterDay(null, DateUtils.now());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingDate2() {
        DateUtils.isAfterDay(DateUtils.now(), null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingDates() {
        DateUtils.isAfterDay(null, null);
    }
    
}
