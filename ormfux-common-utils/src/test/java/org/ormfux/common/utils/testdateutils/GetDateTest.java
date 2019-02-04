package org.ormfux.common.utils.testdateutils;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.ormfux.common.utils.DateUtils;

public class GetDateTest {
    
    @Test
    public void testSuccessful() {
        final Date expectedDate = getRandomDate();
        final Calendar expectedValues = Calendar.getInstance();
        expectedValues.setTime(expectedDate);
        expectedValues.set(Calendar.HOUR_OF_DAY, 0);
        expectedValues.set(Calendar.MINUTE, 0);
        expectedValues.set(Calendar.SECOND, 0);
        expectedValues.set(Calendar.MILLISECOND, 0);
        
        final Date date = DateUtils.getDate(expectedValues.get(Calendar.YEAR), 
                                            expectedValues.get(Calendar.MONTH) + 1, 
                                            expectedValues.get(Calendar.DAY_OF_MONTH));
        
        assertEquals(expectedValues.getTime(), date);
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMonthMin() {
        DateUtils.getDate(2000, 0, 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMonthMax() {
        DateUtils.getDate(2000, 13, 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDayMin() {
        DateUtils.getDate(2000, 0, 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDayMax28() {
        DateUtils.getDate(2001, 2, 29);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDayMax29() {
        DateUtils.getDate(2000, 2, 30);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDayMax30() {
        DateUtils.getDate(2000, 4, 31);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDayMax31() {
        DateUtils.getDate(2000, 4, 32);
    }
    
    private Date getRandomDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(RandomUtils.nextInt(1000, 4000), RandomUtils.nextInt(0, 11), RandomUtils.nextInt(1, 28));
        
        return calendar.getTime();
    }
}
