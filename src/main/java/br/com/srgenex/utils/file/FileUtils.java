package br.com.srgenex.utils.file;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {

    public static List<JarEntry> getJarDirectoryChildren(Class<?> clazz, String dir) throws IOException {
        CodeSource src = clazz.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            try (JarFile jarFile = new JarFile(new File(jar.toURI()))) {
                List<JarEntry> list = new ArrayList<>();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().startsWith(dir) && !entry.isDirectory()) {
                        list.add(entry);
                    }
                }
                return list;
            } catch (URISyntaxException e) {
                throw new IOException(e);
            }
        }
        return Collections.emptyList();
    }


}
