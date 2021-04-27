package md.support.support.controllers;

import md.support.support.models.Department;
import md.support.support.models.User;
import md.support.support.repo.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public String departmentList(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("department", new Department());
        return "department-list";
    }

    @PostMapping()
    public String addDepartment(Department department, Model model) {
        Department departmentFromDb = departmentRepository.findByName(department.getName());
        if (departmentFromDb != null) {
            model.addAttribute("message", "Department exists!");
            return "redirect:/department";
        }
        departmentRepository.save(department);
        return "redirect:/department";
    }

    @PostMapping("/edit")
    public String departmentEdit(@RequestParam(value = "userId", required = false) Department department
            , @RequestParam("name") String name, Model model) {
        department.setName(name);
        departmentRepository.save(department);
        return "redirect:/department";
    }

    @PostMapping("/remove")
    public String departmentRemove(@RequestParam("userId") Department department) {
        departmentRepository.delete(department);
        return "redirect:/department";
    }
}
