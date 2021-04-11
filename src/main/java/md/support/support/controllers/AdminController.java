package md.support.support.controllers;

import md.support.support.models.User;
import md.support.support.repo.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("/admin-panel")
    public String shopList(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int requestsCountByZero = requestRepository.findByCountRequestStateZeroAndShop(user.getShop());
        int requestsCountByThree = requestRepository.findByCountRequestStateThreeAndShop(user.getShop());
        int requestsCountByOne = requestRepository.findByCountRequestStateOne(user.getShop());
        int requestCountTotal = requestRepository.findByCountRequestTotalAndShop(user.getShop());


        model.addAttribute("requestsCountByZero", requestsCountByZero);
        model.addAttribute("requestsCountByThree", requestsCountByThree);
        model.addAttribute("requestsCountByOne", requestsCountByOne);
        model.addAttribute("requestCountTotal", requestCountTotal);
        model.addAttribute("user", user);
        return "admin-panel";
    }
}
