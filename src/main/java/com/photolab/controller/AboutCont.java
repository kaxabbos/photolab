package com.photolab.controller;

import com.photolab.controller.main.Attributes;
import com.photolab.model.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/about")
public class AboutCont extends Attributes {
    @GetMapping
    public String About(Model model) {
        AddAttributes(model);
        return "about";
    }
}
