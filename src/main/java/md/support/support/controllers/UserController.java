package md.support.support.controllers;

import md.support.support.models.Role;
import md.support.support.models.Shop;
import md.support.support.models.User;
import md.support.support.models.Worker;
import md.support.support.repo.ShopRepository;
import md.support.support.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;

    @GetMapping
    public String userList(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("user", new User());
        return "user-list";
    }

    @PostMapping("/edit")
    public String userEdit(@RequestParam("userId") User user, @RequestParam("username") String username
            , @RequestParam("password") String password, @RequestParam("name") String name
            , @RequestParam("shop") String shop, @RequestParam("roles") Set<Role> roles
            , @RequestParam("phone") String phone, @RequestParam("email") String email, Model model) {
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.getShop().clear();
        user.getShop().add(shopRepository.findByName(shop));
        user.getRoles().clear();
        user.setRoles(roles);
        user.setPhone(phone);
        user.setEmail(email);
        userRepository.save(user);
        return "redirect:/user";
    }

    @PostMapping("/remove")
    public String userRemove(@RequestParam("userId") User user) {
        userRepository.delete(user);
        return "redirect:/user";
    }


}
