package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.util.*;

@Repository
@AllArgsConstructor
public class AccidentHibernate {
    private final SessionFactory sf;

    public void add(Accident accident, int typeId, String[] rIds) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Set<Rule> rules = new HashSet<>();
                for (String rId : rIds) {
                    rules.add(session.get(Rule.class, Integer.parseInt(rId)));
                }
                AccidentType type = session.get(AccidentType.class, typeId);
                accident.setRules(rules);
                accident.setType(type);
                session.save(accident);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    public void update(Accident accident, int typeId, String[] rIds) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Set<Rule> rules = new HashSet<>();
                for (String rId : rIds) {
                    rules.add(session.get(Rule.class, Integer.parseInt(rId)));
                }
                AccidentType type = session.get(AccidentType.class, typeId);
                accident.setRules(rules);
                accident.setType(type);
                session.update(accident);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    public Optional<Accident> findById(int id) {
        Optional<Accident> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                List<Accident> list = session.createQuery("from Accident a join fetch a.rules",
                        Accident.class).getResultList();
                Query query = session.createQuery(
                        "from Accident a join fetch a.type where a.id = :fid and a in :flist",
                        Accident.class);
                query.setParameter("fid", id);
                query.setParameter("flist", list);
                Accident accident = (Accident) query.uniqueResult();
                result = Optional.ofNullable(accident);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public List<Accident> findAll() {
        List<Accident> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                List<Accident> list = session.createQuery("from Accident a join fetch a.rules",
                        Accident.class).getResultList();
                result = session.createQuery(
                                "from Accident a join fetch a.type where a in :flist",
                                Accident.class)
                        .setParameter("flist", list).getResultList();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public List<AccidentType> findAllTypes() {
        List<AccidentType> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("from AccidentType", AccidentType.class)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    public List<Rule> findAllRules() {
        List<Rule> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("from Rule",
                        Rule.class).getResultList();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }
}