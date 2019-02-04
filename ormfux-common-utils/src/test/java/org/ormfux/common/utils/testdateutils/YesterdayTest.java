package org.ormfux.common.utils.testdateutils;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.ormfux.common.utils.DateUtils;

public class YesterdayTest {
    
    @Test
    public void testYesterdayNow() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        assertEquals(calendar.getTimeInMillis(), DateUtils.yesterday().getTime());
    }
    
    @Test
    public void testYesterdayOfDate() {
        final Calendar calendar = Calendar.getInstance();
        final Date randomDate = getRandomDate();
        calendar.setTime(randomDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        
        assertEquals(calendar.getTimeInMillis(), DateUtils.yesterday(randomDate).getTime());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingYesterdayDate() {
        DateUtils.yesterday(null);
    }
    
    private Date getRandomDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(RandomUtils.nextInt(1000, 4000), RandomUtils.nextInt(0, 11), RandomUtils.nextInt(1, 28));
        
        return calendar.getTime();
    }
}
