package org.ormfux.common.utils.testfileutils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;
import org.ormfux.common.utils.FileUtils;

public class ReadFileTest {
    
    @Test
    public void readFile() throws IOException {
        final File filePath = new File(this.getClass().getResource("testfile.txt").getPath());
        final String expectedContent = new String(Files.readAllBytes(filePath.toPath())); // TODO Java11 Files.readString(filePath.toPath());
        assertEquals(expectedContent, FileUtils.readFile(filePath.getPath()));
    }
    
    @Test(expected = NullPointerException.class)
    public void readNullPath() throws IOException {
        FileUtils.readFile(null);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void readNonExistingFile() throws IOException {
        FileUtils.readFile("NOTEXISTING");
    }
}
