package ru.job4j.accidents.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class AccidentMem {
    private final HashMap<Integer, Accident> accidents = new HashMap<>();
    private final HashMap<Integer, AccidentType> types = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public void add(Accident accident) {
        accident.setId(counter.getAndIncrement());
        accidents.put(accident.getId(), accident);
    }

    public void update(Accident accident) {
        if (accidents.containsKey(accident.getId())) {
            accidents.replace(accident.getId(), accident);
        }
    }

    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(accidents.get(id));
    }

    public List<Accident> findAll() {
        return new ArrayList<>(accidents.values());
    }

    public void addTypes(List<AccidentType> list) {
        list.forEach(el -> types.put(el.getId(), el));
    }

    public Optional<AccidentType> findTypeById(int id) {
        return Optional.ofNullable(types.get(id));
    }

    public List<AccidentType> findAllTypes() {
        return new ArrayList<>(types.values());
    }
}
