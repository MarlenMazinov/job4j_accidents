package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

@Controller
@AllArgsConstructor
public class IndexController {
    private final AccidentService service;

    @GetMapping("/index")
    public String index(Model model) {
        service.add(new Accident("Инцидент №1", "Пересечение двойной сплошной линии разметки",
        "г. Краснодар, ул. Мира, д.18"));
        service.add(new Accident("Инцидент №2", "Парковка в неположенном месте",
                "г. Краснодар, ул. Ленина, д.1"));
        service.add(new Accident("Инцидент №3",
                "Водитель ТС не пропустил пешехода в нарушение правил ПДД",
                "г. Краснодар, ул. Семашко, д.115"));
        model.addAttribute("accidents", service.findAll());
        model.addAttribute("user", "Petr Arsentev");
        return "index";
    }
}
