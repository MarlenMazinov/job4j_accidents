package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentMem;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
public class AccidentService {
private final AccidentMem store;

public AccidentService(AccidentMem store) {
    this.store = store;
    store.addTypes(List.of(new AccidentType(0, "Две машины"),
            new AccidentType(1, "Машина и человек"),
            new AccidentType(2, "Машина и велосипед")));
    store.add(new Accident("Инцидент №1", "Столкновение двух ТС вследствие несоблюдения дистанции",
            "г. Краснодар, ул. Мира, д.18", store.findTypeById(0).get()));
    store.add(new Accident("Инцидент №2",
            "Водитель ТС не пропустил пешехода в нарушение правил ПДД",
            "г. Краснодар, ул. Ленина, д.1", store.findTypeById(1).get()));
    store.add(new Accident("Инцидент №3",
            "Водитель ТС столкнулся с велосипедистом",
            "г. Краснодар, ул. Семашко, д.115", store.findTypeById(2).get()));
}

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
}
