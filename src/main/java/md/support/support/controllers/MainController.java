package md.support.support.controllers;

import javax.validation.Valid;



import md.support.support.models.Mail;

import md.support.support.repo.RequestRepository;
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

    @PostMapping(value = "/")
    public String homeRequestAdd(@ModelAttribute @Valid Request request, Model model) {

        requestRepository.save(request);
        //Mail mail = new Mail();
        // mail.sendMail(request.getShop(), request.getMessage(), request.getProblem(), request.getPhone(), request.getName());
        return "modal";
    }

    @GetMapping(value = "/")
    public String home(@ModelAttribute Request request, Model model) {
        return "home";
    }

}