package md.support.support.models;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PDFExporter {

    private List<Request> listRequest;

    public PDFExporter(List<Request> listRequest) {
        this.listRequest = listRequest;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        for (Request request : listRequest) {
            BaseFont bf = BaseFont.createFont("C:\\windows\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf);
            font.setSize(18);
            Paragraph p = new Paragraph("Техническая заявка № " + request.getId(), font);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p);

            BaseFont bd = BaseFont.createFont("C:\\windows\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontBd = new Font(bd);
            font.setSize(14);
            Paragraph newLine = new Paragraph("\n\n");
            Paragraph newLineOne = new Paragraph("\n");
            document.add(newLine);

            Paragraph shop = new Paragraph("Магазин: " + String.valueOf(request.getShop()), fontBd);
            document.add(shop);
            Paragraph name = new Paragraph("Заявитель: " + String.valueOf(request.getName()), fontBd);
            document.add(name);
            Paragraph dateCreated = new Paragraph("Дата подачи заявки: " + String.valueOf(request.getDateCreated()), fontBd);
            document.add(dateCreated);
            Paragraph phone = new Paragraph("Телефон: " + String.valueOf(request.getPhone()), fontBd);
            document.add(phone);
            Paragraph problem = new Paragraph("Тип проблемы: " + String.valueOf(request.getProblem()), fontBd);
            document.add(problem);
            document.add(newLineOne);
            Paragraph dateClose = new Paragraph("Дата закрытия заявки: " + String.valueOf(request.getDateClose()), fontBd);
            document.add(dateClose);
            Paragraph worker = new Paragraph("Исполнитель: " + String.valueOf(request.getWorker().get(0)), fontBd);
            document.add(worker);
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
            Paragraph completed = new Paragraph("Выполнил: " + request.getWorker().get(0) + "                                                              " + "Принял: "+request.getName(), font);
            document.add(completed);
            document.newPage();
        }
        document.close();
    }

}
