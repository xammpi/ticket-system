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


import java.util.List;


@Controller
public class RequestController {

    @Autowired
    private RequestRepository requestRepository;

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

    @GetMapping(value = "/completed-requests")
    public String greetingForm(Request request, Model model) {
        viewRequest(model);


        return "completed-requests";
    }

    @PostMapping("/edit-request-completed")
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
        return "redirect:/completed-requests";
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
        viewRequest(model);

        if (request.getDateSort().length() == 0) {
            viewRequest(model);
            List<Request> requests = requestRepository.findByShop(request.getShop());
            model.addAttribute("requests", requests);
            return "completed-requests";
        }

        if (request.getShop().length() == 0) {
            viewRequest(model);
            List<Request> requests = requestRepository.findByDateSort(request.getDateSort());
            model.addAttribute("requests", requests);
            return "completed-requests";
        }

        viewRequest(model);
        List<Request> requests = requestRepository.findByShopAndDateSort(request.getShop(), request.getDateSort());
        model.addAttribute("requests", requests);
        return "completed-requests";
    }

}
