package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

@Controller
public class AccidentController {
    private final AccidentService service;

    public AccidentController(AccidentService service) {
        this.service = service;
        service.add(new Accident("Инцидент №1", "Пересечение двойной сплошной линии разметки",
                "г. Краснодар, ул. Мира, д.18"));
        service.add(new Accident("Инцидент №2", "Парковка в неположенном месте",
                "г. Краснодар, ул. Ленина, д.1"));
        service.add(new Accident("Инцидент №3",
                "Водитель ТС не пропустил пешехода в нарушение правил ПДД",
                "г. Краснодар, ул. Семашко, д.115"));
    }

    @GetMapping("/accidents")
    public String accidents(Model model) {
        model.addAttribute("accidents", service.findAll());
        model.addAttribute("user", "Petr Arsentev");
        return "accidentsView";
    }

    @GetMapping("/addAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("accident", new Accident());
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
        model.addAttribute("user", "Petr Arsentev");
        return "editAccidentForm";
    }

    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident) {
        service.add(accident);
        return "redirect:/index";
    }

    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident) {
        service.update(accident);
        return "redirect:/index";
    }
}