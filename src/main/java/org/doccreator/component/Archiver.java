package org.doccreator.component;

import org.doccreator.config.EnvironmentsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Component
public class Archiver {
    private final static String OPERATING_ROOM = "D://JavaProjects//monkeywriter//operatingRoom";
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

    public void zip(File folder) throws Exception {
        File[] arrFiles = folder.listFiles();
        if (arrFiles == null) throw new Exception("folder is empty: " + folder.getAbsolutePath());
        List<File> fileList = Arrays.asList(arrFiles);
        for(File file:fileList){
            ZipOutputStream zout = new ZipOutputStream(
                    new FileOutputStream(folder.getAbsolutePath() + "//" + folder.getName() + ".zip")
            );
            FileInputStream fis = new FileInputStream(file);
            ZipEntry entry = new ZipEntry(file.getAbsolutePath());
            zout.putNextEntry(entry);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            zout.write(buffer);
            zout.closeEntry();;
        }
    }
}
