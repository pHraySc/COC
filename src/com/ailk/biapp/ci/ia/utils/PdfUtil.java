package com.ailk.biapp.ci.ia.utils;

import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

public class PdfUtil {
    private static BaseFont BaseFont_ZH = null;

    public PdfUtil() {
    }

    public static Document createPdf(OutputStream os) {
        Document pdfDoc = null;
        pdfDoc = new Document(PageSize.A4, 10.0F, 10.0F, 40.0F, 20.0F);

        try {
            PdfWriter.getInstance(pdfDoc, os);
            pdfDoc.open();
        } catch (DocumentException var3) {
            var3.printStackTrace();
        }

        return pdfDoc;
    }

    public static Document createPdf(File file) {
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        }

        return createPdf((OutputStream)os);
    }

    public static Paragraph addParagraph(Document doc) {
        Font subsectFont = new Font(BaseFont_ZH, 0.0F, 0, new Color(255, 255, 255));
        Paragraph paragraph = new Paragraph("", subsectFont);
        return paragraph;
    }

    public static Table createTable(int columnSize) {
        Table table = null;

        try {
            table = new Table(columnSize);
            table.setBorder(0);
            table.setDefaultHorizontalAlignment(1);
            table.setDefaultVerticalAlignment(1);
            table.setPadding(2.0F);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return table;
    }

    public static Chapter addPdfChapter(Document doc, String chapterName) {
        Font subsectFont = new Font(BaseFont_ZH, 12.0F, 3, new Color(0, 0, 0));
        Paragraph cTitle = new Paragraph(chapterName, subsectFont);
        Chapter chapter = new Chapter(cTitle, 0);
        chapter.setBookmarkOpen(false);
        chapter.setNumberDepth(0);
        return chapter;
    }

    public static Cell createCell(Object obj) {
        return null == obj?createCellText(""):(!(obj instanceof Integer) && !(obj instanceof Double) && !(obj instanceof BigDecimal) && !(obj instanceof Float)?createCellText(obj.toString()):createCellNumber(obj.toString()));
    }

    public static Cell createCellHeader(Object obj) {
        Cell cell = null;

        try {
            Font e = new Font(BaseFont_ZH, 10.0F, 1, new Color(0, 0, 0));
            cell = new Cell(new Chunk(obj.toString(), e));
            cell.setBackgroundColor(Color.lightGray);
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return cell;
    }

    public static Cell createCellHeaderWithBorder(Object obj, boolean top, boolean bottom, boolean left, boolean right) {
        Cell cell = createCellHeader(obj);
        if(top) {
            cell.enableBorderSide(1);
        }

        if(bottom) {
            cell.enableBorderSide(2);
        }

        if(left) {
            cell.enableBorderSide(4);
        }

        if(right) {
            cell.enableBorderSide(8);
        }

        return cell;
    }

    public static Cell createCellObject(Element element) {
        Cell cell = null;

        try {
            cell = new Cell(element);
            cell.setHorizontalAlignment(0);
            cell.setVerticalAlignment(8);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return cell;
    }

    public static Cell createCellText(String obj) {
        Cell cell = null;

        try {
            cell = new Cell(new Chunk(obj.toString(), new Font(BaseFont_ZH, 9.0F, 0, new Color(0, 0, 0))));
            cell.setHorizontalAlignment(0);
            cell.setVerticalAlignment(8);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return cell;
    }

    public static Cell createCellNumber(String obj) {
        Cell cell = null;

        try {
            cell = new Cell(new Chunk(obj.toString(), new Font(BaseFont_ZH, 9.0F, 0, new Color(0, 0, 0))));
            cell.setHorizontalAlignment(2);
            cell.setVerticalAlignment(8);
            cell.setNoWrap(true);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return cell;
    }

    public static void main(String[] args) throws Exception {
        Document doc = createPdf(new File("d:/test.pdf"));
        Image image = Image.getInstance("d:/未来一周气温变化.png");
        Table pdfTable = createTable(2);
        pdfTable.setWidth(100.0F);
        pdfTable.addCell(createCellText("维度：地市、时间\n指标：收入、支出\n过滤条件： 在网时长"), 0, 0);
        pdfTable.addCell(createCellObject(image), 1, 0);
        pdfTable.addCell(createCellText("维度：地市、时间\n指标：收入、支出\n过滤条件： 在网时长"), 0, 1);
        pdfTable.addCell(createCellObject(image), 1, 1);
        doc.add(pdfTable);
        doc.close();
    }

    static {
        try {
            BaseFont_ZH = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", true);
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }
}
