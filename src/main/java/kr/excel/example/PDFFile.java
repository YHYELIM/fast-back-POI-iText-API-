package kr.excel.example;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import static com.itextpdf.kernel.font.PdfFontFactory.*;

public class PDFFile {
    public static void main(String[] args) {
        String dest = "book_table.pdf";
        try {
            new PDFFile().createPdf(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPdf(String dest) throws MalformedURLException, IOException {
        List<Map<String, String>> books = createDummyData();

        // Initialize PDF writer and PDF document
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        // Initialize fonts (상대 경로로 변경)
        String fontPath = "NotoSansKR-VariableFont_wght.ttf";
        PdfFont headerFont = PdfFontFactory.createFont(fontPath);
        PdfFont bodyFont = PdfFontFactory.createFont(fontPath);

        // Initialize table
        float[] columnWidths = {1, 2, 2, 2, 2, 2};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        // Initialize table header cells
        table.addHeaderCell(new Cell().add(new Paragraph("순번").setFont(headerFont)));
        table.addHeaderCell(new Cell().add(new Paragraph("제목").setFont(headerFont)));
        table.addHeaderCell(new Cell().add(new Paragraph("저자").setFont(headerFont)));
        table.addHeaderCell(new Cell().add(new Paragraph("출판사").setFont(headerFont)));
        table.addHeaderCell(new Cell().add(new Paragraph("출판일").setFont(headerFont)));
        table.addHeaderCell(new Cell().add(new Paragraph("이미지").setFont(headerFont)));

        // Add table body cells
        int rowNum = 1;
        for (Map<String, String> book : books) {
            String title = book.get("title");
            String authors = book.get("authors");
            String publisher = book.get("publisher");
            String publishedDate = book.get("publishedDate");
            String thumbnail = book.get("thumbnail");

            table.addCell(new Cell().add(new Paragraph(String.valueOf(rowNum)).setFont(bodyFont)));
            table.addCell(new Cell().add(new Paragraph(title).setFont(bodyFont)));
            table.addCell(new Cell().add(new Paragraph(authors).setFont(bodyFont)));
            table.addCell(new Cell().add(new Paragraph(publisher).setFont(bodyFont)));
            table.addCell(new Cell().add(new Paragraph(publishedDate).setFont(bodyFont)));

            try {
                ImageData imageData = ImageDataFactory.create(new File(thumbnail).toURI().toURL());
                Image img = new Image(imageData);
                table.addCell(new Cell().add(img.setAutoScale(true)));
            } catch (MalformedURLException e) {
                table.addCell(new Cell().add(new Paragraph("이미지 불러오기 실패").setFont(bodyFont)));
            } catch (IOException e) {
                table.addCell(new Cell().add(new Paragraph("이미지 파일 없음").setFont(bodyFont)));
            }

            rowNum++;
        }

        document.add(table);
        document.close();
    }

    private static List<Map<String, String>> createDummyData() {
        List<Map<String, String>> books = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("책 개수를 입력하세요: ");
        int bookCount = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 제거
        for (int i = 1; i <= bookCount; i++) {
            Map<String, String> book = new HashMap<>();
            System.out.printf("\n[ %d번째 책 정보 입력 ]\n", i);
            System.out.print("제목: ");
            book.put("title", scanner.nextLine());
            System.out.print("저자: ");
            book.put("authors", scanner.nextLine());
            System.out.print("출판사: ");
            book.put("publisher", scanner.nextLine());
            System.out.print("출판일(YYYY-MM-DD): ");
            book.put("publishedDate", scanner.nextLine());
            System.out.print("썸네일 파일 경로: ");
            book.put("thumbnail", scanner.nextLine());
            books.add(book);
        }
        scanner.close();
        return books;
    }
}
