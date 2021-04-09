package md.support.support.controllers;

import md.support.support.models.Request;

import md.support.support.models.User;
import md.support.support.repo.ProblemRepository;
import md.support.support.repo.RequestRepository;

import md.support.support.repo.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ApplicationRequest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProblemRepository problemRepository;



    @GetMapping("/current-applications")
    public String applicationsMain(Model model) {

        model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZero());
        model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThree());
        model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne());
        model.addAttribute("requestCountTotal", requestRepository.findByCountRequestTotal());
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        if (String.valueOf(user.getRoles()).contains("ADMIN") || String.valueOf(user.getRoles()).contains("SUPPORT")) {
            List<Request> requests = requestRepository.findByState();
            model.addAttribute("requests", requests);
            return "current-applications";
        }
        List<Request> requests = requestRepository.findByStateAndShop(user.getShop());
        model.addAttribute("requests", requests);
        return "current-applications";
    }

    @PostMapping("/edit-request")
    public String editRequest(@RequestParam("id") long id, @RequestParam String shop, @RequestParam String name,
                              @RequestParam String phone, @RequestParam String problem, @RequestParam String message,
                              @RequestParam String comment) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setShop(shop);
        request.setName(name);
        request.setPhone(phone);
        request.setProblem(problem);
        request.setMessage(message);
        request.setComment(comment);
        requestRepository.save(request);
        return "redirect:/current-applications";
    }

    @PostMapping("/edit-state-three")
    public String editStateThree(@RequestParam("id") long id, @RequestParam String state) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(Integer.parseInt(state));
        requestRepository.save(request);
        return "redirect:/current-applications";
    }

    @PostMapping("/change-comment")
    public String changeComment(@RequestParam("id") long id, @RequestParam String comment) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setComment(comment);
        requestRepository.save(request);
        return "redirect:/current-applications";
    }

    @PostMapping("/current-applications/{id}/state1")
    public String applicationStateOne(@PathVariable(value = "id") long id, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(1);
        requestRepository.save(request);
        return "redirect:/current-applications";
    }

}
