package org.ormfux.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;

/**
 * Utilities for file handling.
 */
public final class FileUtils {
    
    private FileUtils() {
    }
    
    /**
     * Reads the contents of a file into an UTF-8 encoded String.
     * 
     * @param filePath The path to the file.
     * @return The file content as String.
     * 
     * @throws IOException
     */
    public static String readFile(final String filePath) throws IOException {
        return readStream(new FileInputStream(filePath));
    }
    
    /**
     * Reads the contents of a stream into an UTF-8 encoded String.
     * 
     * @param inputStream The data stream.
     * @return The stream as String.
     * 
     * @throws IOException
     */
    public static String readStream(final InputStream inputStream) throws IOException {
        //the fasted way to read the file
        //https://stackoverflow.com/a/35446009
        try {
            final ByteArrayOutputStream sqlBytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            
            while ((length = inputStream.read(buffer)) != -1) {
                sqlBytes.write(buffer, 0, length);
            }
            
            return sqlBytes.toString("UTF-8");
        } finally {
            try {
                inputStream.close();
            } catch (final IOException e) {
                //close silently.
            }
        }
    }
    
    /**
     * Gets the names of all files in the directory, which is part of the class path. Can read from a directory 
     * available as "loose file" or from a directory contained in a {@code *.jar} archive of the application. 
     * 
     * @param directory The fully qualified directory path.
     * @param classPathIndicator The class, which provides the "jar" or "loose file" information. The directory is looked
     *                           up in the jar containing this class.
     * @return The (local!) names of the files and directories contained in the given directory 
     * @throws IOException
     */
    //TODO add module support
    public static List<String> readClassPathDirectoryContent(final String directory, final Class<?> classPathIndicator) throws IOException {
        if (!directory.startsWith("/")) {
            throw new IllegalArgumentException("The directory name must be a full path (i.e. start with '/').");
        }
        
        final URL indicatorUrl = classPathIndicator.getClassLoader().getResource(classPathIndicator.getName().replaceAll("\\.", "/") + ".class");
        
        final List<String> directoryContent;
        
        if (StringUtils.equals("file", indicatorUrl.getProtocol())) {
            directoryContent = readFilesFromLooseDirectory(directory, classPathIndicator);
            
        } else if (StringUtils.equals("jar", indicatorUrl.getProtocol())) {
            directoryContent = readFilesFromJarDirectory(directory, indicatorUrl);
            
        } else {
            throw new IOException("Cannot read directory: " + indicatorUrl);
        }
        
        Collections.sort(directoryContent);
        
        return directoryContent;
        
    }
    
    /**
     * Reads the file and directory names of a directory, which is a loose directory on the class path.
     * 
     * @param directory The fully qualified directory name in the jar file.
     * @param directoryUrl The URL to the loose directory.
     * @return The names of the files and directories in the jar's nested directory.
     * 
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private static List<String> readFilesFromLooseDirectory(final String directory, final Class<?> classPathIndicator) throws IOException {
        final URL directoryUrl = classPathIndicator.getClassLoader().getResource(directory.substring(1));
        
        if (directoryUrl != null) {
            final File directoryFile = new File(directoryUrl.getFile());
            
            if (directoryFile.isDirectory()) {
                return ListUtils.fromArray(directoryFile.list());
            } else {
                throw new FileNotFoundException("This is not a directory: " + directory);
            }
            
        } else {
            return new ArrayList<>();
        }
    }
    
    /**
     * Reads the file and directory names of a directory, which is stored in a jar-archive.
     * 
     * @param directory The fully qualified directory name in the jar file.
     * @param directoryUrl The URL to the jar file. Path must be "{@code file:...}"
     * @return The names of the files and directories in the jar's nested directory.
     * 
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private static List<String> readFilesFromJarDirectory(final String directory, 
                                                          final URL directoryUrl) throws IOException, UnsupportedEncodingException {
        final List<String> directoryContent = new ArrayList<>();
        
        final String jarPath = directoryUrl.getPath().substring("file:".length(), directoryUrl.getPath().indexOf('!'));
        
        final JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        final Enumeration<JarEntry> jarEntries = jar.entries();
        
        while (jarEntries.hasMoreElements()) {
            final JarEntry jarEntry = jarEntries.nextElement();
            final String entryName;
            
            if (!StringUtils.startsWith(jarEntry.getName(), "/")) {
                entryName = '/' + jarEntry.getName();
            } else {
                entryName = jarEntry.getName();
            }
            
            if (StringUtils.startsWith(entryName, directory) && entryName.length() > (directory.length() + 1)) {
                final String subPath = entryName.substring(directory.length() + 1);
                
                final String localEntryName;
                
                if (subPath.indexOf('/') >= 0) {
                    //sub-directory >> add only the sub-directory name
                    localEntryName = subPath.substring(0, subPath.indexOf('/'));
                } else {
                    localEntryName = subPath;
                }
                
                if (!directoryContent.contains(localEntryName)) {
                    directoryContent.add(localEntryName);
                }
            }
            
        }
        
        jar.close();
        
        return directoryContent;
    }
}
