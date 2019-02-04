package org.ormfux.common.db.generators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.ormfux.common.utils.DateUtils;

public class DateNowGeneratorTest {
    
    @Test
    public void testGenerate() {
        Date generatedValue = new DateNowGenerator().generate(null);
        assertNotNull(generatedValue);
        assertTrue(DateUtils.now().getTime() - generatedValue.getTime() < 2000);
        
        generatedValue = new DateNowGenerator().generate("param");
        assertNotNull(generatedValue);
        assertTrue(DateUtils.now().getTime() - generatedValue.getTime() < 2000);
        
        generatedValue = new DateNowGenerator().generate(DateUtils.getDate(2000, 1, 1));
        assertNotNull(generatedValue);
        assertTrue(DateUtils.now().getTime() - generatedValue.getTime() < 2000);
        
    }
    
}
