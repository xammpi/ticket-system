package md.support.support.controllers;

import com.itextpdf.text.DocumentException;
import md.support.support.models.PDFExporter;
import md.support.support.models.Request;
import md.support.support.models.User;
import md.support.support.repo.RequestRepository;
import md.support.support.repo.ShopRepository;
import md.support.support.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.StringUtils;

import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/completed")
public class RequestController {

    private final RequestRepository requestRepository;
    private final WorkerRepository workerRepository;

    public RequestController(RequestRepository requestRepository, WorkerRepository workerRepository) {
        this.requestRepository = requestRepository;
        this.workerRepository = workerRepository;
    }


    @Autowired
    private ShopRepository shopRepository;

    public String requestGetQueryString;

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

    @GetMapping("/requests")
    public String filter(@RequestParam(value = "shop", required = false) String shop,
                         @RequestParam(value = "trip-start", required = false) String dateSort,
                         @RequestParam(value = "worker", required = false) Long worker,
                         Model model, HttpServletRequest request, @PageableDefault Pageable pageable) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("worker", workerRepository.findAll());
        if (String.valueOf(user.getRoles()).contains("USER")) {
            model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZeroAndShop(user.getShop().toString()));
            model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThreeAndShop(user.getShop().toString()));
            model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne(user.getShop().toString()));
            model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTowAndShop(user.getShop().toString()));
            model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFourAndShop(user.getShop().toString()));
            model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotalAndShop(user.getShop().toString()));
            model.addAttribute("shops", shopRepository.findByName(user.getShop().toString()));
        }
        if (String.valueOf(user.getRoles()).contains("ADMIN") || String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZero());
            model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThree());
            model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne());
            model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTow());
            model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFour());
            model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotal());
            model.addAttribute("shops", shopRepository.findAll());
        }
        requestGetQueryString = request.getQueryString();
        int p = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            p = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        model.addAttribute("shop", shop);
        model.addAttribute("date", dateSort);

        if (StringUtils.isEmpty(dateSort)) {
            Page<Request> page = requestRepository.findByShop(shop, PageRequest.of(p, size));
            model.addAttribute("page", page);
            return "requests";
        }
        if (StringUtils.isEmpty(shop)) {
            Page<Request> page = requestRepository.findByDateSortAndShop(dateSort, user.getShop().toString(), PageRequest.of(p, size));
            model.addAttribute("page", page);
            return "requests";
        }

        Page<Request> page = requestRepository.findByShopAndDateSort(shop, dateSort,PageRequest.of(p, size));
        model.addAttribute("page", page);
        return "requests";
    }


}
