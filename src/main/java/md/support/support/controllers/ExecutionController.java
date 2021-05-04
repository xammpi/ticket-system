package md.support.support.controllers;

import md.support.support.models.Request;
import md.support.support.models.Shop;
import md.support.support.models.User;
import md.support.support.repo.ProblemRepository;
import md.support.support.repo.RequestRepository;
import md.support.support.repo.ShopRepository;
import md.support.support.repo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@RequestMapping("/request")
@Controller
public class ExecutionController {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @GetMapping("/new")
    public String newRequest(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        model.addAttribute("workers", workerRepository.findAll());


        if (String.valueOf(user.getRoles()).contains("ADMIN")) {
            model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZero());
            List<Request> requests = requestRepository.findByStateZero();
            model.addAttribute("requests", requests);
            return "new-request";
        }

        if (String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByZero", requestRepository.findCountByStateZeroAndDepartmentId(user.getDepartment().getId()));
            List<Request> requests = requestRepository.findByStateZeroAndDepartmentId(user.getDepartment().getId());
            model.addAttribute("requests", requests);
            return "new-request";
        }

        if (String.valueOf(user.getRoles()).contains("USER")) {
            model.addAttribute("requestsCountByZero", requestRepository.findByCountRequestStateZeroAndShop(user.getShop().get(0).getName()));
            List<Request> requests = requestRepository.findByStateZeroAndShop(user.getShop().get(0).getName());
            model.addAttribute("requests", requests);
            return "new-request";
        }
        return "new-request";
    }

    @GetMapping("/in-progress")
    public String executedRequest(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        model.addAttribute("workers", workerRepository.findAll());


        if (String.valueOf(user.getRoles()).contains("ADMIN")) {
            model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTow());
            List<Request> requests = requestRepository.findByStateTwo();
            model.addAttribute("requests", requests);
            return "in-progress";
        }

        if (String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByTwo", requestRepository.findCountByStateTowAndDepartmentId(user.getDepartment().getId()));
            List<Request> requests = requestRepository.findByStateTwoAndDepartmentId(user.getDepartment().getId());
            model.addAttribute("requests", requests);
            return "in-progress";
        }


        if (String.valueOf(user.getRoles()).contains("USER")) {
            model.addAttribute("requestsCountByTwo", requestRepository.findByCountRequestStateTowAndShop(user.getShop().get(0).getName()));
            List<Request> requests = requestRepository.findByStateTwoAndShop(user.getShop().get(0).getName());
            model.addAttribute("requests", requests);
            return "in-progress";
        }
        return "in-progress";
    }

    @GetMapping("/pending")
    public String pendingRequest(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        model.addAttribute("workers", workerRepository.findAll());


        if (String.valueOf(user.getRoles()).contains("ADMIN")) {
            model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThree());
            List<Request> requests = requestRepository.findByStateThree();
            model.addAttribute("requests", requests);
            return "pending-request";
        }

        if (String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByThree", requestRepository.findCountByStateThreeAndDepartmentId(user.getDepartment().getId()));
            List<Request> requests = requestRepository.findByStateThreeAndDepartmentId(user.getDepartment().getId());
            model.addAttribute("requests", requests);
            return "pending-request";
        }
        if (String.valueOf(user.getRoles()).contains("USER")) {
            model.addAttribute("requestsCountByThree", requestRepository.findByCountRequestStateThreeAndShop(user.getShop().get(0).getName()));
            List<Request> requests = requestRepository.findByStateThreeAndShop(user.getShop().get(0).getName());
            model.addAttribute("requests", requests);
            return "pending-request";
        }
        return "pending-request";
    }

    @GetMapping("/awaiting")
    public String awaitingRequest(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        model.addAttribute("workers", workerRepository.findAll());


        if (String.valueOf(user.getRoles()).contains("ADMIN")) {
            model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFour());
            List<Request> requests = requestRepository.findByStateFour();
            model.addAttribute("requests", requests);
            return "awaiting-confirmation";
        }

        if (String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByFour", requestRepository.findCountByStateFourAndDepartmentId(user.getDepartment().getId()));
            List<Request> requests = requestRepository.findByStateFourAndDepartmentId(user.getDepartment().getId());
            model.addAttribute("requests", requests);
            return "awaiting-confirmation";
        }

        if (String.valueOf(user.getRoles()).contains("USER")) {
            model.addAttribute("requestsCountByFour", requestRepository.findByCountRequestStateFourAndShop(user.getShop().toString()));
            List<Request> requests = requestRepository.findByStateFourAndShop(user.getShop().get(0).getName());
            model.addAttribute("requests", requests);
            return "awaiting-confirmation";
        }
        return "awaiting-confirmation";
    }

    @GetMapping("/completed")
    public String completedRequest(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("problems", problemRepository.findAll());
        model.addAttribute("workers", workerRepository.findAll());


        if (String.valueOf(user.getRoles()).contains("ADMIN")) {
            model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne());
            for (Shop shop : shopRepository.findAll()) {
                shop.setCount(requestRepository.findByCountRequestStateOne(shop.getName()));
                shopRepository.save(shop);
            }
            model.addAttribute("shop", shopRepository.findAllByOrderByNumber());
            return "completed-request";
        }

        if (String.valueOf(user.getRoles()).contains("SUPPORT")) {
            model.addAttribute("requestsCountByOne", requestRepository.findCountByStateOneAndDepartmentId(user.getDepartment().getId()));
            for (Shop shop : shopRepository.findAll()) {
                shop.setCount(requestRepository.findByCountRequestStateOne(shop.getName()));
                shopRepository.save(shop);
            }
            model.addAttribute("shop", shopRepository.findAllByOrderByNumber());
            return "completed-request";
        }

        if (String.valueOf(user.getRoles()).contains("USER")) {
            Shop shop = shopRepository.findByName(user.getShop().get(0).getName());
            shop.setCount(requestRepository.findByCountRequestStateOne(user.getShop().get(0).getName()));
            shopRepository.save(shop);
            model.addAttribute("requestsCountByOne", requestRepository.findByCountRequestStateOne(user.getShop().get(0).getName()));
            model.addAttribute("shop", shopRepository.findByName(user.getShop().get(0).getName()));
            return "completed-request";
        }
        return "completed-request";
    }
}
