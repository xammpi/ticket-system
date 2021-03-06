package md.support.support.controllers;

import javax.validation.Valid;

import md.support.support.models.User;
import md.support.support.repo.ProblemRepository;
import md.support.support.repo.RequestRepository;
import md.support.support.repo.ShopRepository;
import md.support.support.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import md.support.support.models.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class MainController {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/")
    public String homeRequestAdd(@ModelAttribute @Valid Request request, Model model) {
        if (request.getPhone().startsWith("0")) {
            request.setPhone(request.getPhone().substring(1, request.getPhone().length()));
        }
        request.setDepartmentId(request.getProblem().getDepartment().getId());
        requestRepository.save(request);
        // Mail mail = new Mail();
        // mail.sendMail(request.getShop(), request.getMessage(), request.getProblem(), request.getPhone(), request.getName());
        return "modal";
    }

    @GetMapping(value = "/")
    public String home(@ModelAttribute Request request, Model model) {
        String userRole = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        if (userRole.equals("[ROLE_ANONYMOUS]")) {
            model.addAttribute("shops", shopRepository.findAll());
            model.addAttribute("problems", problemRepository.findAll());
        }
        if (userRole.equals("[USER]")) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("shops", shopRepository.findByName(user.getShop().get(0).getName()));
            model.addAttribute("problems", problemRepository.findAll());
            model.addAttribute("name", user.getName());
            model.addAttribute("phone", user.getPhone());
            return "home";
        }
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        return "home";
    }

}