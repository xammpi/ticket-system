package md.support.support.models;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.itextpdf.text.html.HtmlTags.FONT;

public class PDFExporter {

    private List<Request> listRequest;

    public PDFExporter(List<Request> listRequest) {
        this.listRequest = listRequest;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        BaseFont bf = BaseFont.createFont("C:\\windows\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf);
        font.setSize(18);
        Paragraph p = new Paragraph("Техническая заявка", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        BaseFont bd = BaseFont.createFont("C:\\windows\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font fontBd = new Font(bd);
        font.setSize(14);
        Paragraph newLine = new Paragraph("\n\n");
        // Paragraph newLine1 = new Paragraph("\n\n");
        document.add(newLine);
        for (Request request : listRequest) {
            Paragraph shop = new Paragraph("Магазин: " + String.valueOf(request.getShop()), fontBd);
            document.add(shop);
            Paragraph date = new Paragraph("Дата: " + String.valueOf(request.getDateCreated()), fontBd);
            document.add(date);
            Paragraph name = new Paragraph("Полное Имя: " + String.valueOf(request.getName()), fontBd);
            document.add(name);
            Paragraph phone = new Paragraph("Телефон: " + String.valueOf(request.getPhone()), fontBd);
            document.add(phone);
            Paragraph problem = new Paragraph("Тип проблемы: " + String.valueOf(request.getProblem()), fontBd);
            document.add(problem);
            Paragraph nameMessage = new Paragraph("Описание проблемы", font);
            nameMessage.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(nameMessage);
            Paragraph newLineAfterNameMessage = new Paragraph("\n");
            document.add(newLineAfterNameMessage);
            Paragraph message = new Paragraph(String.valueOf(request.getMessage()), fontBd);
            document.add(message);
            document.add(newLineAfterNameMessage);
            document.add(newLineAfterNameMessage);
            document.add(newLineAfterNameMessage);
            document.add(newLineAfterNameMessage);
            Paragraph completed = new Paragraph("Выполнил:                                                                          "+"Принял:", font);
            document.add(completed);
        }

        document.close();

    }

}
