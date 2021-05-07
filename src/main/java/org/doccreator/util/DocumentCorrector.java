package org.doccreator.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentCorrector {
    public static void removeMarks(XWPFDocument doc) throws InvalidFormatException, IOException {
        List<XWPFParagraph> deletedParagraphs  = doc.getParagraphs().stream()
                .filter(p -> StringUtils.equalsIgnoreCase("//del", p.getParagraphText()))
                .collect(Collectors.toList());
        for(XWPFParagraph deletedParagraph: deletedParagraphs){
            doc.removeBodyElement(doc.getPosOfParagraph(deletedParagraph));
        }
    }
}
