package com.photolab.controller;

import com.photolab.controller.main.Attributes;
import com.photolab.model.*;
import com.photolab.model.enums.StatusOrdering;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/notes")
public class NotesCont extends Attributes {

    @GetMapping("/add")
    public String noteAdd(Model model) {
        AddAttributesNoteAdd(model);
        return "noteAdd";
    }

    @GetMapping("/my")
    public String orderingsMy(Model model) {
        AddAttributesNotesMy(model);
        return "notesMy";
    }

    @GetMapping("/notconf/{id}")
    public String orderingNotConf(@PathVariable Long id) {
        Ordering ordering = orderingRepo.getReferenceById(id);
        ordering.setStatusOrdering(StatusOrdering.NOT_CONF);
        orderingRepo.save(ordering);
        return "redirect:/notes/my";
    }

    @GetMapping("/conf/{id}")
    public String orderingConf(@PathVariable Long id) {
        Ordering ordering = orderingRepo.getReferenceById(id);
        ordering.setStatusOrdering(StatusOrdering.CONF);
        orderingRepo.save(ordering);
        return "redirect:/notes/my";
    }

    @GetMapping("/done/{id}")
    public String orderingDone(@PathVariable Long id) {
        Ordering ordering = orderingRepo.getReferenceById(id);
        ordering.setStatusOrdering(StatusOrdering.DONE);
        ordering.getNote().setNumber(ordering.getNote().getNumber() + 1);
        orderingRepo.save(ordering);
        return "redirect:/notes/my";
    }

    @GetMapping("/{id}")
    public String note(Model model, @PathVariable Long id) {
        AddAttributesNote(model, id);
        return "note";
    }

    @GetMapping("/edit/{id}")
    public String noteEdit(Model model, @PathVariable Long id) {
        AddAttributesNoteEdit(model, id);
        return "noteEdit";
    }

    @GetMapping("/delete/{id}")
    public String noteDelete(@PathVariable Long id) {
        List<Ordering> orderings = orderingRepo.findAllByNote_Id(id);
        for (Ordering i : orderings){
            orderingRepo.deleteById(i.getId());
        }
        notesRepo.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/ordering/{noteId}")
    public String noteOrdering(@RequestParam Long masterId, @RequestParam String date, @RequestParam String time, @PathVariable Long noteId) {
        Masters master = mastersRepo.getReferenceById(masterId);
        Users client = getUser();
        Notes note = notesRepo.getReferenceById(noteId);

        Ordering ordering = new Ordering(master, client, note, date, time);

        orderingRepo.save(ordering);
        return "redirect:/notes/{noteId}";
    }

    @PostMapping("/add")
    public String noteAddNew(Model model, @RequestParam String name, @RequestParam Long categoryId, @RequestParam MultipartFile photo, @RequestParam int price, @RequestParam String description) {
        String res = "";
        if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "notes/" + uuidFile + "_" + photo.getOriginalFilename();
                    photo.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (Exception e) {
                model.addAttribute("message", "Некорректный данные!");
                AddAttributesNoteAdd(model);
                return "noteAdd";
            }
        }

        Notes note = new Notes(name, res, price, description);

        Category category = categoryRepo.getReferenceById(categoryId);

        category.addNote(note);

        categoryRepo.save(category);

        return "redirect:/notes/add";
    }

    @PostMapping("/edit/{id}")
    public String noteEditOld(Model model, @RequestParam String name, @RequestParam Long categoryId, @RequestParam MultipartFile photo, @RequestParam int price, @RequestParam String description, @PathVariable Long id) {
        Notes note = notesRepo.getReferenceById(id);
        if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
            String res = "";
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "notes/" + uuidFile + "_" + photo.getOriginalFilename();
                    photo.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (Exception e) {
                model.addAttribute("message", "Некорректный данные!");
                AddAttributesNoteEdit(model, id);
                return "noteEdit";
            }
            note.setPhoto(res);
        }

        note.setName(name);
        note.setPrice(price);
        note.setDescription(description);

        Category categoryNew = categoryRepo.getReferenceById(categoryId);
        if ((!categoryNew.getId().equals(note.getCategory().getId()))) {
            Category categoryOld = note.getCategory();
            categoryOld.removeNote(note);
            categoryNew.addNote(note);
        }
        notesRepo.save(note);

        return "redirect:/";
    }
}
