package org.doccreator.component;

import fr.opensagres.xdocreport.core.io.XDocArchive;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Component
public class Archiver {
    private final static String OUT_FOLDER = "D://JavaProjects//monkeywriter//buffer";

    public Archiver(){}

    public XDocArchive getArchive(File doc) throws Exception {
        return XDocArchive.readZip(new FileInputStream(doc));
    }

    public InputStream extractFileForRead(XDocArchive archive, String fileName) throws Exception{
        return archive.getEntryInputStream(fileName);
    }

    public Set<ArchivedFile> extractFilesForRead(XDocArchive archive, String contains) throws Exception{
        Set<ArchivedFile> afs = new HashSet<>();
        Set<String> files = archive.getEntryNames();
        for(String file: files){
            if (file.contains(contains)) {
                afs.add(new ArchivedFile(file, extractFileForRead(archive, file)));
            }
        }
        return afs;
    }

    public OutputStream extractFileForTransform(XDocArchive archive, String fileName) throws Exception{
        return archive.getEntryOutputStream(fileName);
    }

    public void putFile(XDocArchive archive, String fileName, InputStream xml) throws Exception{
        XDocArchive.setEntry(archive, fileName, xml);
    }

    public void writeArchive(XDocArchive oldArchive, String newArchiveName) throws Exception{
        OutputStream newArchive = new FileOutputStream(OUT_FOLDER.concat("//" + newArchiveName));
        XDocArchive.writeZip(oldArchive, newArchive);
    }

    public void printContent(XDocArchive archive){
        System.out.println(archive.getEntryNames());
    }
}
