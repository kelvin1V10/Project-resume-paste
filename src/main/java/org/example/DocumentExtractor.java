import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class DocumentExtractor {

    public static String extractText(File file) throws IOException {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".pdf")) {
            return extractTextFromPdf(file);
        } else if (fileName.endsWith(".docx")) {
            return extractTextFromDocx(file);
        } else if (fileName.endsWith(".txt")) {
            return extractTextFromTxt(file);
        } else {
            throw new IOException("Formato de arquivo não suportado: " + fileName);
        }
    }

    private static String extractTextFromTxt(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    private static String extractTextFromPdf(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private static String extractTextFromDocx(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file.getAbsolutePath());
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }
}