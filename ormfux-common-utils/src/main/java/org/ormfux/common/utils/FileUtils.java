package org.ormfux.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
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
     * @param classPathIndicator The class, which provides the "jar" information. The directory is looked
     *                           up in the jar containing this class.
     * @return The (local!) names of the files and directories contained in the given directory 
     * @throws IOException
     */
    //TODO add module support
    public static List<String> readClassPathDirectoryContent(final String directory, final Class<?> classPathIndicator) throws IOException {
        URL directoryUrl = classPathIndicator.getResource(directory);
        
        if (directoryUrl == null) {
            directoryUrl = classPathIndicator.getResource(classPathIndicator.getName().replaceAll(".", "/") + ".class");
        }
        
        final List<String> directoryContent = new ArrayList<>();
        
        if (StringUtils.equals("file", directoryUrl.getProtocol())) {
            //Directory is not located in the jar file with this initializer
            directoryContent.addAll(Arrays.asList(new File(directoryUrl.getFile()).list()));
            
        } else if (StringUtils.equals("jar", directoryUrl.getProtocol())) {
            //We are in a jar file. get the jar entries matching the directory.
            final String jarPath = directoryUrl.getPath().substring(5, directoryUrl.getPath().indexOf('!'));
            
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
        } else {
            throw new IOException("Cannot read directory: " + directoryUrl);
        }
        
        Collections.sort(directoryContent);
        
        return directoryContent;
        
    }
}
