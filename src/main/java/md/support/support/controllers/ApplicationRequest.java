package md.support.support.controllers;

import md.support.support.models.Request;
import md.support.support.repo.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ApplicationRequest {

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("/applications")
    public String applicationsMain(Model model) {
        List<Request> requests = requestRepository.findByState();
        model.addAttribute("requests", requests);
        return "applications";
    }

    @GetMapping("/applications/{id}")
    public String applicationDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Request> requests = requestRepository.findById(id);
        ArrayList<Request> res = new ArrayList<>();
        requests.ifPresent(res::add);
        model.addAttribute("requests", res);
        return "details";
    }


    @PostMapping("/applications/{id}/state1")
    public String applicationStateOne(@PathVariable(value = "id") long id, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(1);
        requestRepository.save(request);
        return "redirect:/applications";
    }

    @PostMapping("/applications/{id}/state3")
    public String applicationStateThree(@PathVariable(value = "id") long id, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(3);
        requestRepository.save(request);
        return "redirect:/applications";
    }

    @GetMapping("/applications/{id}/edit")
    public String applicationDetailsEditGet(@PathVariable(value = "id") long id, Model model) {
        Optional<Request> request = requestRepository.findById(id);
        ArrayList<Request> res = new ArrayList<>();
        request.ifPresent(res::add);
        model.addAttribute("request", res);
        return "edit-applications";
    }

    @PostMapping("applications/{id}/edit")
    public String applicationEditPost(@PathVariable(value = "id") long id, @RequestParam String shop, @RequestParam String name,
                                      @RequestParam String phone, @RequestParam String problem, @RequestParam String message, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setShop(shop);
        request.setName(name);
        request.setPhone(phone);
        request.setProblem(problem);
        request.setMessage(message);
        requestRepository.save(request);
        return "redirect:/applications";
    }

}
