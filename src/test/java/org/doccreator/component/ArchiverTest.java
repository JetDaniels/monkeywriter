package org.doccreator.component;

import fr.opensagres.xdocreport.core.io.XDocArchive;
import org.doccreator.util.DOMOperationUtil;
import org.doccreator.util.DocFileStructure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;


@RunWith(MockitoJUnitRunner.class)
public class ArchiverTest {

    @Test
    public void archiverTest() throws Exception {
        XDocArchive docArchive = XDocArchive.readZip(new FileInputStream(new File("src//main//resources//templates//template1.docx")));
        System.out.println(docArchive.getEntryNames());
    }

    @Test
    public void testXDocArchiver1() throws Exception{
        XDocArchive docArchive = XDocArchive.readZip(new FileInputStream(new File("src//main//resources//templates//templateTest.zip")));
        OutputStream archive = new FileOutputStream("src//main//resources//templates//templateTest2.zip");

        OutputStream inOut = docArchive.getEntryOutputStream("[in].txt");
        InputStream inTxt = new FileInputStream("src//main//resources//templates//in.txt");

        System.out.println(docArchive.getEntryNames());
        XDocArchive.setEntry(docArchive, "[in].txt", inTxt);
        System.out.println(docArchive.getEntryNames());
        XDocArchive.writeZip(docArchive, archive);
    }

}
