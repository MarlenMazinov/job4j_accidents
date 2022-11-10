package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentHibernate;
import ru.job4j.accidents.repository.AccidentJdbcTemplate;
import ru.job4j.accidents.repository.AccidentMem;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@ThreadSafe
@AllArgsConstructor
public class AccidentService {
    private final AccidentHibernate store;

    public void add(Accident accident, int typeId, String[] rIds) {
        store.add(accident, typeId, rIds);
    }

    public void update(Accident accident, int typeId, String[] rIds) {
        store.update(accident, typeId, rIds);
    }

    public Optional<Accident> findById(int id) {
        return store.findById(id);
    }

    public List<Accident> findAll() {
        return store.findAll();
    }

    /*public Optional<AccidentType> findTypeById(int id) {
        return store.findTypeById(id);
    }*/

    public List<AccidentType> findAllTypes() {
        return store.findAllTypes();
    }

    public List<Rule> findAllRules() {
        return store.findAllRules();
    }
}
