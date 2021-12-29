package org.auioc.mods.ahutils.utils.java;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public interface JarUtils {

    static Attributes getManifest(Class<?> clazz) throws MalformedURLException, IOException {
        String path = clazz.getResource(clazz.getSimpleName() + ".class").toString();
        return new Manifest(new URL(path.substring(0, path.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF").openStream()).getMainAttributes();
    }

}
