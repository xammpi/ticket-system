package md.support.support.controllers;

import md.support.support.models.Request;

import md.support.support.models.User;
import md.support.support.models.Worker;
import md.support.support.repo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
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

    @GetMapping("/current-applications")
    public String applicationsMain(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        model.addAttribute("workers", workerRepository.findAll());


        if (String.valueOf(user.getRoles()).contains("ADMIN") || String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZero());
            model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThree());
            model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne());
            model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTow());
            model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFour());
            model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotal());
            List<Request> requests = requestRepository.findByState();
            model.addAttribute("requests", requests);
            return "current-applications";
        }
        if (String.valueOf(user.getRoles()).contains("USER")) {
            model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZeroAndShop(user.getShop()));
            model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThreeAndShop(user.getShop()));
            model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne(user.getShop()));
            model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTowAndShop(user.getShop()));
            model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFourAndShop(user.getShop()));
            model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotalAndShop(user.getShop()));
            List<Request> requests = requestRepository.findByStateAndShop(user.getShop());
            model.addAttribute("requests", requests);
            return "current-applications";
        }
        return "current-applications";
    }

    @PostMapping("/edit-request")
    public String editRequest(@RequestParam("id") long id, @RequestParam String shop, @RequestParam String name,
                              @RequestParam String phone, @RequestParam String problem, @RequestParam String message,
                              @RequestParam String comment, @RequestParam("worker") Long workerId, HttpServletRequest referer) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setShop(shop);
        request.setName(name);
        request.setPhone(phone);
        request.setProblem(problem);
        request.setMessage(message);
        request.setComment(comment);
        request.setWorker(workerRepository.findAllById(Collections.singleton(workerId)));
        requestRepository.save(request);
        return "redirect:" + referer.getHeader("referer");
    }

    @PostMapping("/edit-state-three")
    public String editStateThree(@RequestParam("id") long id, @RequestParam String state
            , HttpServletRequest referer) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(Integer.parseInt(state));
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
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(1);
        requestRepository.save(request);
        return "redirect:" + referer.getHeader("referer");
    }

    @PostMapping("/current-applications/{id}/state2")
    public String applicationStateTwo(@PathVariable(value = "id") long id, @RequestParam(value = "worker") Long workerId,
                                      HttpServletRequest referer, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(2);
        if (workerId == null) {
            return "redirect:/current-applications";
        }
        request.setWorker(workerRepository.findAllById(Collections.singleton(workerId)));
        requestRepository.save(request);
        return "redirect:" + referer.getHeader("referer");
    }

    @PostMapping("/current-applications/{id}/state4")
    public String applicationStateFour(@PathVariable(value = "id") long id
            , HttpServletRequest referer, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(4);
        requestRepository.save(request);
        return "redirect:" + referer.getHeader("referer");
    }

}
