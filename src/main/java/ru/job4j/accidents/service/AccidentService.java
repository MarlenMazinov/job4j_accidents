package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentMem;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@ThreadSafe
@AllArgsConstructor
public class AccidentService {
    private final AccidentMem store;

    public void add(Accident accident) {
        store.add(accident);
    }

    public void update(Accident accident) {
        store.update(accident);
    }

    public Optional<Accident> findById(int id) {
        return store.findById(id);
    }

    public List<Accident> findAll() {
        return store.findAll();
    }

    public Optional<AccidentType> findTypeById(int id) {
        return store.findTypeById(id);
    }

    public List<AccidentType> findAllTypes() {
        return store.findAllTypes();
    }

    public List<Rule> findAllRules() {
        return store.findAllRules();
    }

    public Set<Rule> findRulesByIds(String[] ids) {
        return store.findRulesByIds(ids);
    }
}
