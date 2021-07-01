package org.doccreator.component;

import net.lingala.zip4j.ZipFile;
import org.doccreator.config.EnvironmentsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Component
public class Archiver {
    private final static String OPERATING_ROOM = "D://JavaProjects//monkeywriter//operatingRoom";
    private final static String RESULT_ROOM = "D://JavaProjects//monkeywriter//buffer";
    private final static String TEMPLATE_ROOM = "src//main//resources//templates";

    public Archiver(){}

    public void unzipTemplate(File template) throws Exception {
        File zipFolder = new File(OPERATING_ROOM + "//" + template.getName().replace(".docx",""));
        zipFolder.mkdir();
        String zipPath = OPERATING_ROOM.concat("//" + template.getName().replace(".docx", ".zip"));
        File templateZip = new File(zipPath);
        Files.copy(template.toPath(), templateZip.toPath());
        unzip(templateZip, zipFolder.getAbsolutePath());
    }

    public void unzip(File zipArchive, String zipFolder) throws Exception {
        ZipInputStream zin = new ZipInputStream(new FileInputStream(zipArchive));
        ZipEntry entry;
        String name;
        long size;
        while((entry=zin.getNextEntry())!=null){
            name = entry.getName();
            size = entry.getSize();
            String[] subFolders = name.split("/");
            String lastSubFolder = zipFolder;
            for(int i=0; i<subFolders.length-1; i++){
                lastSubFolder += "//" + subFolders[i];
                File subFolder = new File(lastSubFolder);
                subFolder.mkdir();
            }
            FileOutputStream fout = new FileOutputStream(zipFolder + "//" + name);
            for(int c=zin.read(); c!=-1; c=zin.read()){
                fout.write(c);
            }
            fout.flush();
            zin.closeEntry();
            fout.close();
        }
        zin.close();
    }

    public void zipTemplate(File sourceFolder) throws Exception {
        Path sourceFolderPath = sourceFolder.toPath();
        File zipFile = new File(RESULT_ROOM.concat("//").concat(sourceFolder.getName()).concat(".zip"));
        Path zipPath = zipFile.toPath();
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                Files.copy(file, zos);
                zos.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });
        zos.close();
        zipFile.renameTo(new File(zipFile.getAbsolutePath().replace(".zip", ".docx")));
    }
}
