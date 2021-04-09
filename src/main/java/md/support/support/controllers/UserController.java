package md.support.support.controllers;

import md.support.support.models.Role;
import md.support.support.models.User;
import md.support.support.repo.ShopRepository;
import md.support.support.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "user-list";
    }

    @PostMapping()
    public String userEdit(@RequestParam("userId") User user, @RequestParam("username") String username
            , @RequestParam("name") String name, @RequestParam("shop") String shop, @RequestParam("phone") String phone
            , @RequestParam("email") String email, @RequestParam("roles") Set<Role> roles) {
        user.setUsername(username);
        user.setName(name);
        user.setShop(shop);
        user.setPhone(phone);
        user.setEmail(email);
        user.getRoles().clear();
        user.setRoles(roles);
        userRepository.save(user);
        return "redirect:/user";
    }

    @PostMapping("/remove")
    public String userRemove(@RequestParam("userId") User user) {
        userRepository.delete(user);
        return "redirect:/user";
    }
}
