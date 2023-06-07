package com.photolab.controller;

import com.photolab.controller.main.Attributes;
import com.photolab.model.Masters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/master")
public class MasterCont extends Attributes {
    @GetMapping
    public String Master(Model model) {
        AddAttributesMaster(model);
        return "master";
    }

    @PostMapping("/edit/photo")
    public String MasterEditPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
            String res = "";
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "masters/" + uuidFile + "_" + photo.getOriginalFilename();
                    photo.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (Exception e) {
                model.addAttribute("message", "Некорректный данные!");
                AddAttributesMaster(model);
                return "master";
            }
            Masters master = getUser().getMaster();
            master.setPhoto(res);
            mastersRepo.save(master);
        }
        return "redirect:/master";
    }

    @PostMapping("/edit")
    public String MasterEdit(@RequestParam String fio, @RequestParam Long category, @RequestParam String tel, @RequestParam byte experience) {
        Masters master = getUser().getMaster();
        master.setFio(fio);
        master.setCategory(categoryRepo.getReferenceById(category).getName());
        master.setTel(tel);
        master.setExperience(experience);
        mastersRepo.save(master);
        return "redirect:/master";
    }
}
