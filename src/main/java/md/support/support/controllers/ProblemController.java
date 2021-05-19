package md.support.support.controllers;

import md.support.support.models.Department;
import md.support.support.models.Problem;
import md.support.support.models.User;
import md.support.support.repo.DepartmentRepository;
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

    private final ProblemRepository problemRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public ProblemController(ProblemRepository problemRepository, DepartmentRepository departmentRepository) {
        this.problemRepository = problemRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping()
    public String problemList(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("problems", problemRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("problem", new Problem());
        return "problem-list";
    }

    @PostMapping()
    public String addNewProblemModal(Problem problem, Model model) {
        problemRepository.save(problem);
        return "redirect:/problem";
    }

    @PostMapping("/edit-problem")
    public String editProblemModal(@RequestParam("id") Problem problem, @RequestParam("name") String name
            , @RequestParam("department") Department department) {
        problem.setName(name);
        problem.setDepartment(department);
        problemRepository.save(problem);
        return "redirect:/problem";
    }

    @PostMapping("/remove")
    public String problemRemove(@RequestParam("id") Problem problem) {
        problemRepository.delete(problem);
        return "redirect:/problem";
    }

}
