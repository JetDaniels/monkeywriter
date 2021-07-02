package org.doccreator.component;

import lombok.Getter;

import java.io.InputStream;

@Getter
public class ArchivedFile {
    private String fileName;
    private InputStream inputStream;

    public ArchivedFile(String fileName, InputStream inputStream){
        this.fileName = fileName;
        this.inputStream = inputStream;
    }
}
