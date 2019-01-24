package org.ormfux.common.utils.testdateutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.ormfux.common.utils.DateUtils;

public class TodayTest {
    
    @Test
    public void testWithoutTimeZone() {
        final Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        
        final Date today = DateUtils.today();
        assertNotNull(today);
        assertEquals(now.getTimeInMillis(), today.getTime());
    }
    
    @Test
    public void testWithTimeZone() {
        final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        
        final Date today = DateUtils.today(TimeZone.getTimeZone("UTC"));
        assertNotNull(today);
        assertEquals(now.getTimeInMillis(), today.getTime());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingTimeZone() {
        DateUtils.today(null);
    }
    
}
