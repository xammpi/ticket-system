package md.support.support.controllers;

import md.support.support.models.*;

import md.support.support.repo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;

@Controller
public class ApplicationRequest {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/current-applications")
    public String applicationsMain(@RequestParam(value = "departmentId", required = false) Long departmentId
            , Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        model.addAttribute("workers", workerRepository.findAll());

        if (String.valueOf(user.getRoles()).contains("ADMIN")) {
            model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZero());
            model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThree());
            model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne());
            model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTow());
            model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFour());
            model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotal());
            model.addAttribute("name", user.getName());
            model.addAttribute("phone", user.getPhone());
            model.addAttribute("department", departmentRepository.findAll());
            if (!StringUtils.isEmpty(departmentId)) {
                model.addAttribute("requests", requestRepository.findByDepartmentId(departmentId));
                return "current-applications";
            }
            model.addAttribute("requests", requestRepository.findByState());
            return "current-applications";
        }

        if (String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByZero", requestRepository.findCountByStateZeroAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestsCountByThree", requestRepository.findCountByStateThreeAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestsCountByOne", requestRepository.findCountByStateOneAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestsCountByTwo", requestRepository.findCountByStateTowAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestsCountByFour", requestRepository.findCountByStateFourAndDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requestCountTotal", requestRepository.findCountTotalByDepartmentId(user.getDepartment().getId()));
            List<Request> requests = requestRepository.findByDepartmentId(user.getDepartment().getId());
            model.addAttribute("name", user.getName());
            model.addAttribute("phone", user.getPhone());
            model.addAttribute("workers", workerRepository.findByDepartmentId(user.getDepartment().getId()));
            model.addAttribute("requests", requests);
            return "current-applications";
        }


        if (String.valueOf(user.getRoles()).contains("USER")) {
            for (Shop p : user.getShop()) {
                model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZeroAndShop(p.getName()));
                model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThreeAndShop(p.getName()));
                model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne(p.getName()));
                model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTowAndShop(p.getName()));
                model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFourAndShop(p.getName()));
                model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotalAndShop(p.getName()));
                model.addAttribute("requests", requestRepository.findByStateAndShop(p.getName()));
                model.addAttribute("shops", shopRepository.findByName(p.getName()));
                model.addAttribute("name", user.getName());
                model.addAttribute("phone", user.getPhone());

                return "current-applications";
            }
        }
        return "current-applications";
    }

    @PostMapping(value = "/add-request")
    public String homeRequestAdd(@ModelAttribute @Valid Request request, Model model) {
        request.setDepartmentId(request.getProblem().getDepartment().getId());
        requestRepository.save(request);

        // Mail mail = new Mail();
        // mail.sendMail(request.getShop(), request.getMessage(), request.getProblem(), request.getPhone(), request.getName());
        return "redirect:/current-applications";
    }


    @PostMapping("/edit-request")
    public String editRequest(@RequestParam("id") Request request, @RequestParam String shop, @RequestParam String name,
                              @RequestParam String phone, @RequestParam Problem problem, @RequestParam String message,
                              @RequestParam String comment, @RequestParam("workerId") String workerId, HttpServletRequest referer) {

        request.setShop(shop);
        request.setName(name);
        request.setPhone(phone);
        request.setProblem(problem);
        request.setMessage(message);
        request.setComment(comment);
        request.setDepartmentId(problem.getDepartment().getId());
        request.getWorker().add(workerRepository.findByName(workerId));
        requestRepository.save(request);
        return "redirect:" + referer.getHeader("referer");
    }


    @PostMapping("/change-comment")
    public String changeComment(@RequestParam("id") long id, @RequestParam String comment
            , HttpServletRequest referer) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setComment(comment);
        requestRepository.save(request);
        return "redirect:" + referer.getHeader("referer");
    }

    @PostMapping("/current-applications/{id}/state1")
    public String applicationStateOne(@PathVariable(value = "id") long id, HttpServletRequest referer
            , Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Request request = requestRepository.findById(id).orElseThrow();

        if (String.valueOf(user.getRoles()).contains("ADMIN")) {
            request.setState(1);
            request.setDateClose(Calendar.getInstance().getTime());
            request.setExecution(Calendar.getInstance().getTime());
            request.setDateEnd(Calendar.getInstance().getTime());
            if (request.getWorker().size() == 0) {
                request.getWorker().add(workerRepository.findByName("ADMIN"));
            }
            requestRepository.save(request);
            return "redirect:" + referer.getHeader("referer");
        }
        if (String.valueOf(user.getRoles()).contains("USER")) {
            if (request.getExecution() != null && request.getDateEnd() != null) {
                request.setState(1);
                request.setDateClose(Calendar.getInstance().getTime());
                requestRepository.save(request);
                return "redirect:" + referer.getHeader("referer");
            }
        }
        return "redirect:" + referer.getHeader("referer");
    }

    @PostMapping("/current-applications/{id}/state2")
    public String applicationStateTwo(@PathVariable(value = "id") long id, @RequestParam(value = "worker") Long workerId,
                                      HttpServletRequest referer, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(2);
        request.getWorker().clear();
        request.setWorker(workerRepository.findWorkerById(workerId));
        request.setExecution(Calendar.getInstance().getTime());
        requestRepository.save(request);
        return "redirect:" + referer.getHeader("referer");
    }

    @PostMapping("/current-applications/{id}/state3")
    public String editStateThree(@PathVariable(value = "id") long id
            , HttpServletRequest referer) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(3);
        requestRepository.save(request);
        return "redirect:" + referer.getHeader("referer");
    }

    @PostMapping("/current-applications/{id}/state4")
    public String applicationStateFour(@PathVariable(value = "id") long id
            , HttpServletRequest referer) throws InterruptedException {
        Request request = requestRepository.findById(id).orElseThrow();
        if (request.getWorker().size() != 0) {
            request.setState(4);
            request.setDateEnd(Calendar.getInstance().getTime());
            requestRepository.save(request);
            try {
                Mail mail = new Mail();
                List<User> user = userRepository.findUserByName(request.getName());
                if (user.size() != 0) {
                    mail.sendEmailToConfirmationRequest(request, user.get(0).getEmail());
                }
            } catch (NullPointerException e) {

            }
        }
        return "redirect:/current-applications";
    }

}
