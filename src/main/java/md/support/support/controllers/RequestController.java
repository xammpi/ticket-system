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
public class RequestController {

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping(value = "/completed-requests")
    public String greetingForm(Request request, Model model) {
        //List<Request> requests = null;
        // model.addAttribute("requests", requests);
        return "completed-requests";
    }

    @GetMapping("/completed-requests/{id}")
    public String allRequestDetail(@PathVariable(value = "id") long id, Model model) {
        Optional<Request> requests = requestRepository.findById(id);
        ArrayList<Request> res = new ArrayList<>();
        requests.ifPresent(res::add);
        model.addAttribute("requests", res);
        return "all-request-detail";
    }

    @GetMapping("/completed-requests/{id}/edit")
    public String applicationDetailsEditGet(@PathVariable(value = "id") long id, Model model) {
        Optional<Request> request = requestRepository.findById(id);
        ArrayList<Request> res = new ArrayList<>();
        request.ifPresent(res::add);
        model.addAttribute("request", res);
        return "edit-request";
    }

    @PostMapping("/completed-requests/{id}/state0")
    public String applicationStateZero(@PathVariable(value = "id") long id, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setState(0);
        requestRepository.save(request);
        return "redirect:/completed-requests";
    }

    @PostMapping(value = "/completed-requests")
    public String filter(Request request, Model model) {
        if (request.getDateSort().length() == 0) {
            List<Request> requests = requestRepository.findByShop(request.getShop());
            model.addAttribute("requests", requests);
            return "completed-requests";
        }
        if (request.getShop().length() == 0) {
            List<Request> requests = requestRepository.findByDateSort(request.getDateSort());
            model.addAttribute("requests", requests);
            return "completed-requests";
        }
        List<Request> requests = requestRepository.findByShopAndDateSort(request.getShop(), request.getDateSort());
        model.addAttribute("requests", requests);
        return "completed-requests";
    }

    @PostMapping("/completed-requests/{id}/edit")
    public String applicationEditPost(@PathVariable(value = "id") long id, @RequestParam String shop, @RequestParam String name,
                                      @RequestParam String phone, @RequestParam String problem, @RequestParam String message, Model model) {
        Request request = requestRepository.findById(id).orElseThrow();
        request.setShop(shop);
        request.setName(name);
        request.setPhone(phone);
        request.setProblem(problem);
        request.setMessage(message);
        requestRepository.save(request);
        return "redirect:/completed-requests";
    }
}
