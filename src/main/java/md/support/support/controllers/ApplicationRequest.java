package md.support.support.controllers;

import md.support.support.models.Request;
import md.support.support.repo.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ApplicationRequest {

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("/current-applications")
    public String applicationsMain(Model model) {
        int requestsCountByZero = requestRepository.findByCountRequestStateZero();
        int requestsCountByThree = requestRepository.findByCountRequestStateThree();
        int requestsCountTotal = requestRepository.findByCountRequest();

        model.addAttribute("requestsCountByZero", requestsCountByZero);
        model.addAttribute("requestsCountByThree", requestsCountByThree);
        model.addAttribute("requestsCountTotal", requestsCountTotal);

        List<Request> requests = requestRepository.findByState();
        model.addAttribute("requests", requests);

        return "current-applications";
    }

    @GetMapping("/current-applications/{id}")
    public String applicationDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Request> requests = requestRepository.findById(id);
        ArrayList<Request> res = new ArrayList<>();
        requests.ifPresent(res::add);
        model.addAttribute("requests", res);
        return "details-applications";
    }

    @PostMapping("/change-comment")
    public String applicationComment(@RequestParam("id") long id, @RequestParam("comment") String comment) {
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

    @PostMapping("/current-applications/{id}/state3")
    public String applicationStateThree(@PathVariable(value = "id") long id, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(3);
        requestRepository.save(request);
        return "redirect:/current-applications";
    }

    @GetMapping("/current-applications/{id}/edit")
    public String applicationDetailsEditGet(@PathVariable(value = "id") long id, Model model) {
        Optional<Request> request = requestRepository.findById(id);
        ArrayList<Request> res = new ArrayList<>();
        request.ifPresent(res::add);
        model.addAttribute("request", res);
        return "edit-applications";
    }

    @PostMapping("current-applications/{id}/edit")
    public String applicationEditPost(@PathVariable(value = "id") long id, @RequestParam String shop, @RequestParam String name,
                                      @RequestParam String phone, @RequestParam String problem, @RequestParam String message,
                                      @RequestParam String comment, Model model) {
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

}
