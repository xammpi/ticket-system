package md.support.support.controllers;

import com.itextpdf.text.DocumentException;
import md.support.support.models.*;
import md.support.support.repo.ProblemRepository;
import md.support.support.repo.RequestRepository;
import md.support.support.repo.ShopRepository;
import md.support.support.repo.WorkerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
    private final ProblemRepository problemRepository;
    private final ShopRepository shopRepository;

    public RequestController(RequestRepository requestRepository
            , WorkerRepository workerRepository
            , ProblemRepository problemRepository
            , ShopRepository shopRepository) {
        this.requestRepository = requestRepository;
        this.workerRepository = workerRepository;
        this.problemRepository = problemRepository;
        this.shopRepository = shopRepository;
    }

    @PostMapping("/edit")
    public String edit(HttpServletResponse response
            , @RequestParam(value = "id", required = false) List<Long> s
            , @RequestParam String action, HttpServletRequest referer, Model model) {
        if (s == null) {
            //return "requests";
            return "redirect:" + referer.getHeader("referer");
        }
        if (action.equals("Вернуть")) {
            for (Long q : s) {
                Request r = requestRepository.findById(q).orElseThrow();
                r.setState(0);
                requestRepository.save(r);
            }

            return "redirect:" + referer.getHeader("referer");
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
                return "redirect:" + referer.getHeader("referer");
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:" + referer.getHeader("referer");
    }

    @GetMapping("/requests")
    public String filter(@RequestParam(value = "shop", required = false) String shop,
                         @RequestParam(value = "trip-start", required = false) String dateSort,
                         @RequestParam(value = "worker", required = false) String worker,
                         @RequestParam(value = "problem", required = false) String problem,
                         Model model, Pageable pageable, HttpServletRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("worker", workerRepository.findAll());
        model.addAttribute("problem", problemRepository.findAll());
        model.addAttribute("shop", shop);
        model.addAttribute("date", dateSort);
        model.addAttribute("work", worker);

            model.addAttribute("prob", problem);

        int p = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            p = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }


        if (String.valueOf(user.getRoles()).contains("USER")) {
            for (Shop shopTmp : user.getShop()) {
                model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZeroAndShop(shopTmp.getName()));
                model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThreeAndShop(shopTmp.getName()));
                model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne(shopTmp.getName()));
                model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTowAndShop(shopTmp.getName()));
                model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFourAndShop(shopTmp.getName()));
                model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotalAndShop(shopTmp.getName()));
                model.addAttribute("shops", shopRepository.findByName(shopTmp.getName()));

                Page<Request> page = requestRepository.findByCurrentDayAndShop(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())
                        , user.getShop().get(0).getName(), PageRequest.of(p, size));
                model.addAttribute("page", page);

                if (!StringUtils.isEmpty(shop)) {
                    page = requestRepository.findByShop(shop, PageRequest.of(p, size));
                    model.addAttribute("page", page);
                    return "requests";
                }
                if (!StringUtils.isEmpty(dateSort)) {
                    page = requestRepository.findByDateSortAndShop(dateSort, user.getShop().get(0).getName()
                            , PageRequest.of(p, size));
                    model.addAttribute("page", page);
                    return "requests";
                }
            }
        }

        if (String.valueOf(user.getRoles()).contains("ADMIN")) {
            model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZero());
            model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThree());
            model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne());
            model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTow());
            model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFour());
            model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotal());
            model.addAttribute("shops", shopRepository.findAll());

            Page<Request> page = requestRepository.findByCurrentDay(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()), PageRequest.of(p, size));
            model.addAttribute("page", page);

            if (!StringUtils.isEmpty(problem)) {
                page = requestRepository.findByProblemNameAndState(problem, 1, PageRequest.of(p, size));
                model.addAttribute("page", page);
                return "requests";
            }

            if (!StringUtils.isEmpty(shop)) {
                page = requestRepository.findByShop(shop, PageRequest.of(p, size));
                model.addAttribute("page", page);
                return "requests";
            }
            if (!StringUtils.isEmpty(dateSort)) {
                page = requestRepository.findByDateSort(dateSort, PageRequest.of(p, size));
                model.addAttribute("page", page);
                return "requests";
            }
            if (!StringUtils.isEmpty(worker)) {
                page = requestRepository.findByWorkerIdAndState(Long.parseLong(worker), 1, PageRequest.of(p, size));
                model.addAttribute("page", page);
                return "requests";
            }
        }
        if (String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByZero", requestRepository.findCountByStateZeroAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestsCountByThree", requestRepository.findCountByStateThreeAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestsCountByOne", requestRepository.findCountByStateOneAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestsCountByTwo", requestRepository.findCountByStateTowAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestsCountByFour", requestRepository.findCountByStateFourAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestCountTotal", requestRepository.findCountTotalByDepartmentId(user.getDepartment().getId()));
            model.addAttribute("shops", shopRepository.findAll());
            model.addAttribute("problem", problemRepository.findAllByDepartmentId(user.getDepartment().getId()));

            Page<Request> page = requestRepository.findByCurrentDayAndDepartmentId(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())
                    , user.getDepartment().getId(), PageRequest.of(p, size));
            model.addAttribute("page", page);

            if (!StringUtils.isEmpty(problem)) {
                page = requestRepository.findByProblemNameAndState(problem, 1, PageRequest.of(p, size));
                model.addAttribute("page", page);
                return "requests";
            }

            if (!StringUtils.isEmpty(shop)) {
                page = requestRepository.findByShopAndDepartmentId(shop, user.getDepartment().getId(), PageRequest.of(p, size));
                model.addAttribute("page", page);
                return "requests";
            }
            if (!StringUtils.isEmpty(dateSort)) {
                page = requestRepository.findByDateSortAndDepartmentId(dateSort, user.getDepartment().getId(), PageRequest.of(p, size));
                model.addAttribute("page", page);
                return "requests";
            }
            if (!StringUtils.isEmpty(worker)) {
                page = requestRepository.findByWorkerIdAndState(Long.parseLong(worker), 1, PageRequest.of(p, size));
                model.addAttribute("page", page);
                return "requests";
            }
        }

        return "requests";
    }


}
