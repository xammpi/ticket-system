package md.support.support.controllers;

import md.support.support.models.Mail;
import md.support.support.models.Shop;
import md.support.support.models.User;
import md.support.support.repo.RequestRepository;
import md.support.support.repo.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopController {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("/shop")
    public String shopList(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("shops", shopRepository.findAll());
        return "shop-list";
    }

    @PostMapping("/add-shop")
    public String addNewShopModal(Shop shop, Model model) {
        shopRepository.save(shop);
        return "redirect:/shop";
    }

    @PostMapping("/edit-shop")
    public String editShopModal(@RequestParam("id") Shop shop, @RequestParam("name") String name) {
        shop.setName(name);
        shopRepository.save(shop);
        return "redirect:/shop";
    }

    @PostMapping("/remove")
    public String shopRemove(@RequestParam("id") Shop shop) {
        shopRepository.delete(shop);
        return "redirect:/shop";
    }


}
