package md.support.support.controllers;

import md.support.support.models.Mail;
import md.support.support.models.Role;
import md.support.support.models.User;
import md.support.support.repo.ShopRepository;
import md.support.support.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("shops", shopRepository.findAll());
        return "registration";
    }

    @PostMapping("/registration")
    public String addNewUser(User user, Model model) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        Mail mail = new Mail();
        mail.sendMailToUser(user.getEmail(), user.getName(), user.getShop().toString(), user.getUsername(), user.getPassword());
        return "redirect:/login";
    }

    @PostMapping("/registration-modal-user")
    public String addNewUserModal(User user, Model model) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        user.setActive(true);
        userRepository.save(user);
        Mail mail = new Mail();
        mail.sendMailToUser(user.getEmail(), user.getName(), user.getShop().toString(), user.getUsername(), user.getPassword());
        return "redirect:/user";
    }

}
