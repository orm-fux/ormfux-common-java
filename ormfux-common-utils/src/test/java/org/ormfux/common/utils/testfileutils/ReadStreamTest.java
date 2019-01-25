package org.ormfux.common.utils.testfileutils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.ormfux.common.utils.FileUtils;

public class ReadStreamTest {
    
    @Test
    public void readStream() throws IOException {
        final String streamContent = "this is a test\ntext";
        final InputStream inputStream = new ByteArrayInputStream(streamContent.getBytes());
        final String readContent = FileUtils.readStream(inputStream);
        
        assertEquals(streamContent, readContent);
        assertEquals(-1, inputStream.read());
    }
    
    @Test(expected = NullPointerException.class)
    public void readNullStream() throws IOException {
        FileUtils.readStream(null);
    }
    
}
