package md.support.support.controllers;

import com.itextpdf.text.DocumentException;
import md.support.support.models.PDFExporter;
import md.support.support.models.Request;
import md.support.support.repo.RequestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/completed")
public class RequestController {

    private final RequestRepository requestRepository;

    public RequestController(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public String requestGetQueryString;

    public void viewRequest(Model model) {
        int requestsCountByZero = requestRepository.findByCountRequestStateZero();
        int requestsCountByThree = requestRepository.findByCountRequestStateThree();
        int requestsCountByOne = requestRepository.findByCountRequestStateOne();
        int requestCountTotal = requestRepository.findByCountRequestTotal();

        model.addAttribute("requestsCountByZero", requestsCountByZero);
        model.addAttribute("requestsCountByThree", requestsCountByThree);
        model.addAttribute("requestsCountByOne", requestsCountByOne);
        model.addAttribute("requestCountTotal", requestCountTotal);
    }

    @PostMapping("/edit")
    public String edit(HttpServletResponse response
            , @RequestParam(value = "id", required = false) List<Long> s
            , @RequestParam String action, Model model) {
        if (s == null) {
            return "requests";
        }
        if (action.equals("Вернуть")) {
            for (Long q : s) {
                Request r = requestRepository.findById(q).orElseThrow();
                r.setState(0);
                requestRepository.save(r);
            }

            return "redirect:/completed/requests?" + requestGetQueryString;
        }
        if (action.equals("Сохранить")) {
            response.setContentType("application/pdf; charset=UTF-8");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=Request_" + currentDateTime + ".pdf";
            response.setHeader(headerKey, headerValue);

            List<Request> res = new ArrayList<Request>();
            for (Long q : s) {
                List<Request> requests = requestRepository.findByIdAll(q);
                res.add(requests.get(0));
            }
            PDFExporter exporter = new PDFExporter(res);
            try {
                exporter.export(response);
                return "redirect:/completed/requests?" + requestGetQueryString;
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "requests";
    }

    /*
        @PostMapping("/requests/{id}/state0")
        public String applicationStateZero(@PathVariable(value = "id") long id, Model model) {
            Request request = requestRepository.findById(id).orElseThrow();
            request.setState(0);
            requestRepository.save(request);
            return "redirect:/requests";
        }
     */
    @GetMapping("/requests")
    public String filter(@RequestParam(value = "shop", required = false) String shop,
                         @RequestParam(value = "trip-start", required = false) String dateSort,
                         Model model, HttpServletRequest request) {
        viewRequest(model);
        int requestsCountByZeroAndShop = requestRepository.findByCountRequestStateOneAndShop(shop);
        model.addAttribute("shopSelect", shop);
        model.addAttribute("requestsCountByZeroAndShop",requestsCountByZeroAndShop);
        requestGetQueryString = request.getQueryString();
        if (StringUtils.isEmpty(dateSort)) {
            List<Request> requests = requestRepository.findByShop(shop);
            model.addAttribute("requests", requests);
            return "requests";
        }
        if (StringUtils.isEmpty(shop)) {
            List<Request> requests = requestRepository.findByDateSort(dateSort);
            model.addAttribute("requests", requests);
            return "requests";
        }
        List<Request> requests = requestRepository.findByShopAndDateSort(shop, dateSort);
        model.addAttribute("requests", requests);
        return "requests";
    }
}
