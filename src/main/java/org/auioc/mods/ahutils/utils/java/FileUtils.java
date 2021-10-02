package org.auioc.mods.ahutils.utils.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.auioc.mods.ahutils.utils.LogUtil;

public interface FileUtils {

    static Path getOrCreateDirectory(String directoryName) throws Exception {
        Path path = Paths.get(directoryName);
        if (!path.toFile().exists()) {
            try {
                Files.createDirectory(path);
                LogUtil.warn(LogUtil.getMarker("FileUtils"), "Folder " + path.toAbsolutePath() + " does not exist, created automatically.");
            } catch (final IOException e) {
                LogUtil.error(LogUtil.getMarker("FileUtils"), "Could not create directory!", e);
                throw new Exception("Could not create directory " + path);
            }
        }
        return path;
    }

    static File writeText(String directoryName, String fileName, StringBuffer buffer) throws Exception {
        Path path = getOrCreateDirectory(directoryName);
        File file = new File(path.toUri().getPath() + "/" + fileName);
        if (file.exists()) {
            LogUtil.warn(LogUtil.getMarker("FileUtils"), "File " + file + " already exists, overwrite.");
        }
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(buffer.toString());
            writer.close();
        } catch (final Exception e) {
            LogUtil.error(LogUtil.getMarker("FileUtils"), "Cannot write data to file!", e);
            throw new Exception("Cannot write data to file " + file);
        }
        return file;
    }

}
