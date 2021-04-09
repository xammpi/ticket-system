package md.support.support.controllers;

import javax.validation.Valid;


import md.support.support.models.Mail;

import md.support.support.repo.ProblemRepository;
import md.support.support.repo.RequestRepository;
import md.support.support.repo.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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

    @PostMapping(value = "/")
    public String homeRequestAdd(@ModelAttribute @Valid Request request, Model model) {


        requestRepository.save(request);
        // Mail mail = new Mail();
        // mail.sendMail(request.getShop(), request.getMessage(), request.getProblem(), request.getPhone(), request.getName());
        return "modal";
    }

    @GetMapping(value = "/")
    public String home(@ModelAttribute Request request, Model model) {
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        return "home";
    }

}