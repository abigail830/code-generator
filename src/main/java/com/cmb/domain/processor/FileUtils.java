package com.cmb.domain.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

public class FileUtils {

    public static void copyFileOrFolder(String source, String dest) throws IOException {
        File sourceFile = new File(source);
        File targetFile = new File(dest);
        copyFileOrFolder(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFileOrFolder(File source, File dest, CopyOption... options) throws IOException {
        if (source.isDirectory())
            copyFolder(source, dest, options);
        else {
            ensureParentFolder(dest);
            copyFile(source, dest, options);
        }
    }

    private static void copyFolder(File source, File dest, CopyOption... options) throws IOException {
        if (!dest.exists())
            dest.mkdirs();
        File[] contents = source.listFiles();
        if (contents != null) {
            for (File f : contents) {
                File newFile = new File(dest.getAbsolutePath() + File.separator + f.getName());
                if (f.isDirectory())
                    copyFolder(f, newFile, options);
                else
                    copyFile(f, newFile, options);
            }
        }
    }

    private static void copyFile(File source, File dest, CopyOption... options) throws IOException {
        Files.copy(source.toPath(), dest.toPath(), options);
    }

    private static void ensureParentFolder(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists())
            parent.mkdirs();
    }

    public static void delete(String filePath) {
        try {
            Files.walk(Paths.get(filePath)).sorted(Comparator.reverseOrder()).map(Path::toFile)
                    .peek(System.out::println).forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
