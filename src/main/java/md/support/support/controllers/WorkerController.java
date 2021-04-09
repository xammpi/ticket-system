package md.support.support.controllers;

import md.support.support.models.Position;
import md.support.support.models.User;
import md.support.support.models.Worker;
import md.support.support.repo.PositionRepository;
import md.support.support.repo.WorkerRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private PositionRepository positionRepository;

    @GetMapping
    public String workerList(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("workers", workerRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        model.addAttribute("worker", new Worker());
        return "worker-list";
    }

    @PostMapping()
    public String addWorker(Worker worker, Model model) {
        Worker userFromDb = workerRepository.findByName(worker.getName());
        if (userFromDb != null) {
            model.addAttribute("message", "Worker exists!");
            return "redirect:/worker";
        }
        workerRepository.save(worker);
        return "redirect:/worker";
    }

    @PostMapping("/remove")
    public String userRemove(@RequestParam("userId") Worker worker) {
        workerRepository.delete(worker);
        return "redirect:/worker";
    }


    @PostMapping("/edit")
    public String workerEdit(@RequestParam(value = "userId", required = false) Worker worker, @RequestParam("name") String name,
                             @RequestParam("position") Long position, Model model) {
        worker.setName(name);
        worker.getPosition().clear();
        worker.getPosition().add(positionRepository.findById(position).orElseThrow());
        workerRepository.save(worker);
        return "redirect:/worker";
    }
}
