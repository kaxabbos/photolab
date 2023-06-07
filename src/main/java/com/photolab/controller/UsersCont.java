package com.photolab.controller;

import com.photolab.controller.main.Attributes;
import com.photolab.model.Masters;
import com.photolab.model.Users;
import com.photolab.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersCont extends Attributes {
    @GetMapping
    public String subs(Model model) {
        AddAttributesUsers(model);
        return "users";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @RequestParam Role role) {
        Users user = usersRepo.getReferenceById(id);

        if (role != Role.MASTER && user.getRole() == Role.MASTER) {
            Masters master = user.getMaster();
            user.setMaster(null);
            mastersRepo.deleteById(master.getId());
        }
        if (role == Role.MASTER && user.getRole() != Role.MASTER) {
            Masters master = new Masters(defPhoto);
            mastersRepo.save(master);
            user.setMaster(master);
        }

        user.setRole(role);
        usersRepo.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(Model model, @PathVariable Long id) {
        Users user = usersRepo.getReferenceById(id);
        if (user == getUser()) {
            AddAttributesUsers(model);
            model.addAttribute("message", "Вы не можете удалить свой профиль!");
            return "users";
        }
        usersRepo.deleteById(id);
        return "redirect:/users";
    }
}
