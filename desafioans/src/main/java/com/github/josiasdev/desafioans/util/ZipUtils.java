package com.github.josiasdev.desafioans.util;

import java.io.*;
import java.util.zip.*;
import java.nio.file.*;

public class ZipUtils {
    public static void compactarParaZip(String sourceFile, String zipName) throws IOException {
        Path p = Files.createFile(Paths.get(zipName));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceFile);
            ZipEntry zipEntry = new ZipEntry(pp.getFileName().toString());
            zs.putNextEntry(zipEntry);
            Files.copy(pp, zs);
            zs.closeEntry();
        }
    }
}