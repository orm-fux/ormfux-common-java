package org.ormfux.common.utils.testdateutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.ormfux.common.utils.DateUtils;

public class GetCalendarTest {
    
    @Test
    public void testWithoutTimeZone() {
        final Calendar expectedCalendar = getRandomCalendar(null);
        final Calendar createdCalendar = DateUtils.getCalendar(expectedCalendar.getTime(), null);
        
        expectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
        expectedCalendar.set(Calendar.MINUTE, 0);
        expectedCalendar.set(Calendar.SECOND, 0);
        expectedCalendar.set(Calendar.MILLISECOND, 0);
        
        assertNotNull(createdCalendar);
        assertEquals(expectedCalendar.getTimeInMillis(), createdCalendar.getTimeInMillis());
        assertEquals(TimeZone.getDefault(), createdCalendar.getTimeZone());
    }
    
    @Test
    public void testWithTimeZone() {
        final TimeZone timeZone = TimeZone.getTimeZone("UTC");
        final Calendar expectedCalendar = getRandomCalendar("UTC");
        final Calendar createdCalendar = DateUtils.getCalendar(expectedCalendar.getTime(), timeZone);
        
        expectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
        expectedCalendar.set(Calendar.MINUTE, 0);
        expectedCalendar.set(Calendar.SECOND, 0);
        expectedCalendar.set(Calendar.MILLISECOND, 0);
        
        assertNotNull(createdCalendar);
        assertEquals(expectedCalendar.getTimeInMillis(), createdCalendar.getTimeInMillis());
        assertEquals(timeZone, createdCalendar.getTimeZone());
    }
    
    @Test
    public void testWithNullDate() {
        final Calendar expectedCalendar = Calendar.getInstance();
        final Calendar createdCalendar = DateUtils.getCalendar(null, null);
        
        expectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
        expectedCalendar.set(Calendar.MINUTE, 0);
        expectedCalendar.set(Calendar.SECOND, 0);
        expectedCalendar.set(Calendar.MILLISECOND, 0);
        
        assertNotNull(createdCalendar);
        assertEquals(expectedCalendar.getTimeInMillis(), createdCalendar.getTimeInMillis());
        assertEquals(TimeZone.getDefault(), createdCalendar.getTimeZone());
        
    }
    
    private Calendar getRandomCalendar(String timeZone) {
        final Calendar calendar;
        
        if (!StringUtils.isBlank(timeZone)) {
            calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            calendar = Calendar.getInstance();
        }
        
        calendar.set(RandomUtils.nextInt(1000, 4000), RandomUtils.nextInt(0, 11), RandomUtils.nextInt(1, 28));
        
        return calendar;
    }
}
