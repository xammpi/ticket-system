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
        int requestsCountByOne = requestRepository.findByCountRequestStateOne();
        int requestCountTotal = requestRepository.findByCountRequestTotal();

        model.addAttribute("requestsCountByZero", requestsCountByZero);
        model.addAttribute("requestsCountByThree", requestsCountByThree);
        model.addAttribute("requestsCountByOne", requestsCountByOne);
        model.addAttribute("requestCountTotal", requestCountTotal);

        List<Request> requests = requestRepository.findByState();
        model.addAttribute("requests", requests);

        return "current-applications";
    }

    /*  @GetMapping("/current-applications/{id}")
      public String applicationDetails(@PathVariable(value = "id") long id, Model model) {
          Optional<Request> requests = requestRepository.findById(id);
          ArrayList<Request> res = new ArrayList<>();
          requests.ifPresent(res::add);
          model.addAttribute("requests", res);
          return "details-applications";
      }


     */
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
