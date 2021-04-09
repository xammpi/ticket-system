package md.support.support.controllers;

import md.support.support.models.Position;
import md.support.support.models.User;
import md.support.support.models.Worker;
import md.support.support.repo.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionRepository positionRepository;

    @GetMapping
    public String addPosition(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("positions", positionRepository.findAll());
        return "position-list";
    }

    @PostMapping()
    public String addPosition (Position position, Model model) {
        Position positionFromDb = positionRepository.findByName(position.getName());
        if (positionFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "position-list";
        }
        positionRepository.save(position);
        return "redirect:/position";
    }

    @PostMapping("/edit")
    public String workerEdit(@RequestParam("userId") Position position, @RequestParam("name") String name) {
        position.setName(name);

        positionRepository.save(position);
        return "redirect:/position";
    }


    @PostMapping("/remove")
    public String userRemove(@RequestParam("userId") Position position) {
        positionRepository.delete(position);
        return "redirect:/position";
    }
}
