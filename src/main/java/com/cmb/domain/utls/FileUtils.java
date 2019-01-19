package com.cmb.domain.utls;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    public static Optional<Path> findFile(Path path, String fileName) {
        try {
            return Files.list(Paths.get(path.toUri()))
                    .filter(Files::isRegularFile)
                    .filter(p -> Objects.equals(p.getFileName().toString(), fileName))
                    .findAny();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Path> listDirectory(String path) {
        try {
            return Files.list(Paths.get(path)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isDiretory(String path) {
        return Files.isDirectory(Paths.get(path));
    }

    public static void copyFile(String sourcePath, String newPath) {
        File file = new File(sourcePath);
        if (file.isFile()) {
            File newFile = new File(newPath);
            try (
                    FileInputStream in = new FileInputStream(file);
                    FileOutputStream out = new FileOutputStream(newFile);
            ) {
                int readByte;
                while ((readByte = in.read()) != -1) {
                    out.write(readByte);
                }
            } catch (IOException e) {
                //
            }
        }
    }

    public static void writeTo(String filePathName, String content) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePathName), "utf-8"));
            writer.write(content);
        } catch (IOException ex) {
            // Report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }

    public static void createDirectory(String path) {
        if (!Files.isDirectory(Paths.get(path))) {
            new File(path).mkdirs();
        }
    }

    public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
    }

    public static void delete(String filePath){
        try {
            Files.walk(Paths.get(filePath)).sorted(Comparator.reverseOrder()).map(Path::toFile)
                    .peek(System.out::println).forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
