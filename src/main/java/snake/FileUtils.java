package snake;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {

    public static List<Image> extractImages(String zipFilePath) {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            return zipFile.stream().map(entry -> {
                try {
                    return new Image(zipFile.getInputStream(entry));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image extractImage(String zipFilePath, String imgName) {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().equals(imgName)) {
                    return new Image(zipFile.getInputStream(entry));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    static List<Image> loadImages(String dir) {
        return Stream.of(new File(dir).listFiles())
                .map(File::getPath)
                .map(FileUtils::loadImage)
                .collect(Collectors.toList());
    }

    public static Image loadImage(String path) {
        try {
            return new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
