package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

@Controller
@AllArgsConstructor
public class AccidentController {
    private final AccidentService service;

    @GetMapping("/accidents")
    public String accidents(Model model) {
        model.addAttribute("accidents", service.findAll());
        model.addAttribute("user", "Petr Arsentev");
        return "accidentsView";
    }

    @GetMapping("/addAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("accident", new Accident());
        model.addAttribute("types", service.findAllTypes());
        model.addAttribute("user", "Petr Arsentev");
        return "createAccidentForm";
    }

    @GetMapping("/editAccident/{accidentId}")
    public String viewEditAccident(Model model, @PathVariable("accidentId") int id) {
        Accident accident = new Accident();
        if (service.findById(id).isPresent()) {
            accident = service.findById(id).get();
        }
        model.addAttribute("accident", accident);
        model.addAttribute("types", service.findAllTypes());
        model.addAttribute("user", "Petr Arsentev");
        return "editAccidentForm";
    }

    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident,
                       @RequestParam(value = "type.id") int typeId) {
        if (service.findTypeById(typeId).isPresent()) {
            accident.setType(service.findTypeById(typeId).get());
        }
        service.add(accident);
        return "redirect:/index";
    }

    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident,
                         @RequestParam(value = "type.id") int typeId) {
        if (service.findTypeById(typeId).isPresent()) {
            accident.setType(service.findTypeById(typeId).get());
        }
        service.update(accident);
        return "redirect:/index";
    }
}