package org.doccreator.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

@RunWith(MockitoJUnitRunner.class)
public class WordReaderTest {

    WordReader reader = new WordReader(new Archiver());

    @Test
    public void wordReaderTest1() throws Exception {
        reader.searchLinks(new File("src//main//resources//templates//template1.docx"));
    }

    @Test
    public void wordReaderTest2() throws Exception {
        reader.searchLinks(new File("src//main//resources//templates//template3.docx"));
    }

    @Test
    public void wordReaderTest3() throws Exception {
        reader.searchLinks(new File("src//main//resources//templates//template4.docx"));
    }
}
