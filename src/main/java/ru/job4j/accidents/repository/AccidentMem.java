package ru.job4j.accidents.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class AccidentMem {
    private final HashMap<Integer, Accident> accidents = new HashMap<>();
    private final HashMap<Integer, AccidentType> types = new HashMap<>();

    private final HashMap<Integer, Rule> rules = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public AccidentMem() {
        addTypes(List.of(new AccidentType(0, "Две машины"),
                new AccidentType(1, "Машина и человек"),
                new AccidentType(2, "Машина и велосипед")));
        addRules(List.of(new Rule(3, "Статья 1"),
                new Rule(4, "Статья 2"),
                new Rule(5, "Статья 3")));
        add(new Accident("Инцидент №1", "Столкновение двух ТС вследствие несоблюдения дистанции",
                "г. Краснодар, ул. Мира, д.18", findTypeById(0).get(),
                Set.of(findRuleById(3).get())));
        add(new Accident("Инцидент №2",
                "Водитель ТС не пропустил пешехода в нарушение правил ПДД",
                "г. Краснодар, ул. Ленина, д.1", findTypeById(1).get(),
                Set.of(findRuleById(3).get(), findRuleById(4).get())));
        add(new Accident("Инцидент №3",
                "Водитель ТС столкнулся с велосипедистом",
                "г. Краснодар, ул. Семашко, д.115", findTypeById(2).get(),
                Set.of(findRuleById(5).get())));
    }

    public void add(Accident accident) {
        accident.setId(counter.getAndIncrement());
        accidents.put(accident.getId(), accident);
    }

    public void update(Accident accident) {
        accidents.replace(accident.getId(), accident);
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

    public void addRules(List<Rule> list) {
        list.forEach(el -> rules.put(el.getId(), el));
    }

    public Optional<Rule> findRuleById(int id) {
        return Optional.ofNullable(rules.get(id));
    }

    public List<Rule> findAllRules() {
        return new ArrayList<>(rules.values());
    }

    public Set<Rule> findRulesByIds(String[] ids) {
        Set<Rule> set = new HashSet<>();
        for (int i = 0; i < ids.length; i++) {
            set.add(findRuleById(Integer.parseInt(ids[i])).orElseThrow());
        }
        return set;
    }
}
