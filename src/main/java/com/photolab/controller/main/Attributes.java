package com.photolab.controller.main;

import com.photolab.model.Category;
import com.photolab.model.Masters;
import com.photolab.model.Notes;
import com.photolab.model.Users;
import com.photolab.model.enums.Role;
import org.springframework.ui.Model;

import java.util.List;

public class Attributes extends Main {

    protected void AddAttributes(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());
    }

    protected void AddAttributesUsers(Model model) {
        AddAttributes(model);
        model.addAttribute("users", usersRepo.findAll());
        model.addAttribute("roles", Role.values());
    }

    protected void AddAttributesNoteAdd(Model model) {
        AddAttributes(model);
        model.addAttribute("categories", categoryRepo.findAll());
    }

    protected void AddAttributesNoteEdit(Model model, Long id) {
        AddAttributes(model);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("note", notesRepo.getReferenceById(id));
    }

    protected void AddAttributesNotesMy(Model model) {
        AddAttributes(model);
        Users user = getUser();
        if (user.getRole() == Role.MASTER) {
            model.addAttribute("orderings", user.getMaster().getOrderings());
        } else {
            model.addAttribute("orderings", user.getOrderings());
        }
    }

    protected void AddAttributesNote(Model model, Long id) {
        AddAttributes(model);
        Notes note = notesRepo.getReferenceById(id);
        List<Masters> masters = mastersRepo.findAllByCategory(note.getCategory().getName());
        model.addAttribute("note", note);
        model.addAttribute("masters", masters);
    }

    protected void AddAttributesIndex(Model model) {
        AddAttributes(model);
        model.addAttribute("notes", notesRepo.findAll());
        model.addAttribute("categories", categoryRepo.findAll());
    }

    protected void AddAttributesCategory(Model model) {
        AddAttributes(model);
        model.addAttribute("categories", categoryRepo.findAll());
    }

    protected void AddAttributesMaster(Model model) {
        AddAttributes(model);
        model.addAttribute("categories", categoryRepo.findAll());
    }

    protected void AddAttributesMasters(Model model) {
        AddAttributes(model);
        model.addAttribute("masters", mastersRepo.findAll());
    }

    protected void AddAttributesSearch(Model model, String name, Long categoryId) {
        AddAttributes(model);
        Category category = categoryRepo.getReferenceById(categoryId);
        model.addAttribute("notes", notesRepo.findAllByNameContainingAndCategory_Name(name, category.getName()));
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("selectedCId", categoryId);
        model.addAttribute("name", name);
    }

    protected void AddAttributesStats(Model model) {
        AddAttributes(model);
        model.addAttribute("notes", notesRepo.findAll());
    }
}
