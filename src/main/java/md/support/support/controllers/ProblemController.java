package md.support.support.controllers;

import md.support.support.models.Problem;
import md.support.support.models.User;
import md.support.support.repo.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/problem")
@Controller
public class ProblemController {

    @Autowired
    private ProblemRepository problemRepository;

    @GetMapping()
    public String problemList(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("problems", problemRepository.findAll());
        return "problem-list";
    }

    @PostMapping("/add-problem")
    public String addNewProblemModal(Problem problem, Model model) {
        problemRepository.save(problem);
        return "redirect:/problem";
    }

    @PostMapping("/edit-problem")
    public String editProblemModal(@RequestParam("id") Problem problem, @RequestParam("name") String name) {
        problem.setName(name);
        problemRepository.save(problem);
        return "redirect:/problem";
    }

    @PostMapping("/remove")
    public String problemRemove(@RequestParam("id") Problem problem) {
        problemRepository.delete(problem);
        return "redirect:/problem";
    }

}
