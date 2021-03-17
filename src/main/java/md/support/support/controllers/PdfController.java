package md.support.support.controllers;


import com.itextpdf.text.DocumentException;
import md.support.support.models.PDFExporter;
import md.support.support.models.Request;
import md.support.support.repo.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class PdfController {

    @Autowired
    RequestRepository requestRepository;


    @PostMapping("/export/pdf")
    public void editRequest(HttpServletResponse response, @RequestParam("id") long id) throws IOException, DocumentException {
        response.setContentType("application/pdf; charset=UTF-8");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Request_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Request> requests = (List<Request>) requestRepository.findByIdAll(id);

        PDFExporter exporter = new PDFExporter(requests);
        exporter.export(response);

    }


}
