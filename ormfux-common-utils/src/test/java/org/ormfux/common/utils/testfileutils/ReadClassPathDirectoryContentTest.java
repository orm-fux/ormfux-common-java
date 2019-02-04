package org.ormfux.common.utils.testfileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runners.Suite;
import org.ormfux.common.utils.FileUtils;

public class ReadClassPathDirectoryContentTest {
    
    @Test
    public void testReadFromJar() throws IOException {
        final List<String> dirContent = FileUtils.readClassPathDirectoryContent('/' + Suite.class.getPackageName().replaceAll("\\.", "/"), Test.class);
        assertNotNull(dirContent);
        assertEquals(18, dirContent.size());
        assertTrue(dirContent.contains("AllTests.class"));
        assertTrue(dirContent.contains("BlockJUnit4ClassRunner.class"));
        assertTrue(dirContent.contains("JUnit4.class"));
        assertTrue(dirContent.contains("MethodSorters.class"));
        assertTrue(dirContent.contains("package-info.class"));
        assertTrue(dirContent.contains("Parameterized.class"));
        assertTrue(dirContent.contains("ParentRunner.class"));
        assertTrue(dirContent.contains("Suite.class"));
        assertTrue(dirContent.contains("model"));
        
        assertTrue(dirContent.contains("BlockJUnit4ClassRunner$1.class"));
        assertTrue(dirContent.contains("ParentRunner$1.class"));
        assertTrue(dirContent.contains("ParentRunner$2.class"));
        assertTrue(dirContent.contains("ParentRunner$3.class"));
        assertTrue(dirContent.contains("ParentRunner$4.class"));
        assertTrue(dirContent.contains("Parameterized$Parameter.class"));
        assertTrue(dirContent.contains("Parameterized$Parameters.class"));
        assertTrue(dirContent.contains("Parameterized$TestClassRunnerForParameters.class"));
    }
    
    @Test
    public void readFromLooseDirectory() throws IOException {
        final List<String> dirContent = FileUtils.readClassPathDirectoryContent('/' + getClass().getPackageName().replaceAll("\\.", "/"), FileUtils.class);
        assertNotNull(dirContent);
        assertEquals(4, dirContent.size());
        assertTrue(dirContent.contains("testfile.txt"));
        assertTrue(dirContent.contains(getClass().getSimpleName() + ".class"));
        assertTrue(dirContent.contains(ReadFileTest.class.getSimpleName() + ".class"));
        assertTrue(dirContent.contains(ReadStreamTest.class.getSimpleName() + ".class"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonFullDirectoryPathLoose() throws IOException {
        FileUtils.readClassPathDirectoryContent(FileUtils.class.getPackageName().replaceAll("\\.", "/"), FileUtils.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonFullDirectoryPathJar() throws IOException {
        FileUtils.readClassPathDirectoryContent(Suite.class.getPackageName().replaceAll("\\.", "/"), Test.class);
    }
    
    @Test
    public void testDirectoryNotInJar() throws IOException {
        final List<String> dirContent = FileUtils.readClassPathDirectoryContent("/nonexisting/directory", Test.class);
        assertNotNull(dirContent);
        assertEquals(0, dirContent.size());
    }
    
    @Test
    public void testDirectoryNotInLoose() throws IOException {
        final List<String> dirContent = FileUtils.readClassPathDirectoryContent("/nonexisting/directory", FileUtils.class);
        assertNotNull(dirContent);
        assertEquals(0, dirContent.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullDirectoryName() throws IOException {
        FileUtils.readClassPathDirectoryContent(null, FileUtils.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullClasspathReference() throws IOException {
        FileUtils.readClassPathDirectoryContent("/nonexisting/directory", null);
    }
}
