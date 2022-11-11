package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@ThreadSafe
@AllArgsConstructor
public class AccidentService {
    private final AccidentRepository accidentStore;
    private final AccidentTypeRepository accidentTypeStore;
    private final RuleRepository ruleStore;

    public void saveOrUpdate(Accident accident, String[] rIds) {
        Set<Rule> rules = new HashSet<>();
        if (rIds != null && rIds.length > 0) {
            for (String rId : rIds) {
                rules.add(ruleStore.findById(Integer.parseInt(rId)).get());
            }
        }
        accident.setRules(rules);
        accidentStore.save(accident);
    }

    public Optional<Accident> findById(int id) {
        return accidentStore.findById(id);
    }

    public List<Accident> findAll() {
        return accidentStore.findAll();
    }

    public List<AccidentType> findAllTypes() {
        return (List<AccidentType>) accidentTypeStore.findAll();
    }

    public List<Rule> findAllRules() {
        return (List<Rule>) ruleStore.findAll();
    }
}
