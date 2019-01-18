package org.blue.bunny.common.utils.testdateutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.blue.bunny.common.utils.DateUtils;
import org.junit.Test;

public class IsSameOrAfterDayTest {
    
    @Test
    public void sameDay() {
        Calendar calendar1 = DateUtils.getCalendar(null, null);
        Calendar calendar2 = DateUtils.getCalendar(null, null);
        assertTrue(DateUtils.isSameOrAfterDay(calendar1.getTime(), calendar2.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 1);
        assertTrue(DateUtils.isSameOrAfterDay(calendar1.getTime(), calendar2.getTime()));
        assertTrue(DateUtils.isSameOrAfterDay(calendar2.getTime(), calendar1.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 999);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.HOUR, 23);
        assertTrue(DateUtils.isSameOrAfterDay(calendar1.getTime(), calendar2.getTime()));
        assertTrue(DateUtils.isSameOrAfterDay(calendar2.getTime(), calendar1.getTime()));
        
    }
    
    @Test
    public void beforeDay() {
        Calendar calendar1 = DateUtils.getCalendar(DateUtils.getDate(2000, 1, 1), null);
        Calendar calendar2 = DateUtils.getCalendar(DateUtils.getDate(2000, 1, 2), null);
        assertFalse(DateUtils.isSameOrAfterDay(calendar1.getTime(), calendar2.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 1);
        assertFalse(DateUtils.isSameOrAfterDay(calendar1.getTime(), calendar2.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 999);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.HOUR, 23);
        assertFalse(DateUtils.isSameOrAfterDay(calendar1.getTime(), calendar2.getTime()));
    }
    
    @Test
    public void afterDay() {
        Calendar calendar1 = DateUtils.getCalendar(DateUtils.getDate(2000, 1, 1), null);
        Calendar calendar2 = DateUtils.getCalendar(DateUtils.getDate(2000, 1, 2), null);
        assertTrue(DateUtils.isSameOrAfterDay(calendar2.getTime(), calendar1.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 1);
        assertTrue(DateUtils.isSameOrAfterDay(calendar2.getTime(), calendar1.getTime()));
        
        calendar1.set(Calendar.MILLISECOND, 999);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.HOUR, 23);
        assertTrue(DateUtils.isSameOrAfterDay(calendar2.getTime(), calendar1.getTime()));
    }
    
}
