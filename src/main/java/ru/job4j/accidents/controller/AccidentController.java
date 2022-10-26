package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Controller
@AllArgsConstructor
public class AccidentController {
    private final AccidentService service;

    @GetMapping("/accidents")
    public String accidents(Model model) {
        model.addAttribute("accidents", service.findAll());
        model.addAttribute("rules", service.findAllRules());
        model.addAttribute("user", "Petr Arsentev");
        return "accidentsView";
    }

    @GetMapping("/addAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("accident", new Accident());
        model.addAttribute("types", service.findAllTypes());
        model.addAttribute("rules", service.findAllRules());
        model.addAttribute("user", "Petr Arsentev");
        return "createAccidentForm";
    }

    @GetMapping("/editAccident/{accidentId}")
    public String viewEditAccident(Model model, @PathVariable("accidentId") int id) throws Exception {
        Accident accident = service.findById(id)
                .orElseThrow(() -> new Exception("No accident found with id " + id));
        model.addAttribute("accident", accident);
        model.addAttribute("types", service.findAllTypes());
        model.addAttribute("rules", service.findAllRules());
        model.addAttribute("user", "Petr Arsentev");
        return "editAccidentForm";
    }

    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident,
                       @RequestParam(value = "type.id") int typeId,
                       HttpServletRequest req) throws Exception {
        accident.setType(service.findTypeById(typeId)
                .orElseThrow(() -> new Exception("No accident type found with id " + typeId)));
        String[] ids = req.getParameterValues("rIds");
        accident.setRules(service.findRulesByIds(ids));
        service.add(accident);
        return "redirect:/index";
    }

    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident,
                         @RequestParam(value = "type.id") int typeId,
                         HttpServletRequest req) throws Exception {
        accident.setType(service.findTypeById(typeId)
                .orElseThrow(() -> new Exception("No accident type found with id " + typeId)));
        String[] ids = req.getParameterValues("rIds");
        accident.setRules(service.findRulesByIds(ids));
        service.update(accident);
        return "redirect:/index";
    }
}