package org.ormfux.common.db.generators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.ormfux.common.utils.DateUtils;
import org.ormfux.common.utils.object.Objects;

public class OnceNowGeneratorTest {
    
    @Test
    public void testGenerateFirstValue() {
        Date generatedValue = new OnceDateNowGenerator().generate(null);
        assertNotNull(generatedValue);
        assertTrue(DateUtils.now().getTime() - generatedValue.getTime() < 2000);
    }
    
    @Test
    public void testGenerateFollowupValue() {
        Date date = DateUtils.getDate(2000, 1, 1);
        Date generatedValue = new OnceDateNowGenerator().generate(date);
        assertNotNull(generatedValue);
        assertTrue(Objects.isSame(date, generatedValue));
        
    }
    
    @Test(expected = ClassCastException.class)
    public void testWrongParamType() {
        new OnceDateNowGenerator().generate("param");
    }
    
}
