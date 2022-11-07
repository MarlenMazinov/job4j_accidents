package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;

    public Accident add(Accident accident, String[] rIds) {
        String insertSql = "insert into accidents (name, text, address, type_id) "
                + "values (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement statement =
                    con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, accident.getName());
            statement.setString(2, accident.getText());
            statement.setString(3, accident.getAddress());
            statement.setInt(4, accident.getType().getId());
            return statement;
        }, keyHolder);
        Integer id = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        for (String rId : rIds) {
            jdbc.update(
                    "insert into accident_rules (accident_id, rule_id) values (?, ?)",
                    id, Integer.parseInt(rId));
        }
        return accident;
    }

    public Accident update(Accident accident, String[] rIds) {
        jdbc.update("update accidents set name = ?, text = ?, address = ?, type_id = ? "
                        + "where id = ?",
                accident.getName(), accident.getText(), accident.getAddress(),
                accident.getType().getId(), accident.getId());
        jdbc.update("delete from accident_rules where accident_id = ?",
                accident.getId());
        for (String rId : rIds) {
            jdbc.update("insert into accident_rules (accident_id, rule_id) values (?, ?)",
                    accident.getId(), Integer.parseInt(rId));
        }
        return accident;
    }

    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(jdbc.queryForObject("select a.name name,"
                        + "       a.text text,"
                        + "       a.address address,"
                        + "       t.id typeId, t.name typeName"
                        + "       from accidents a"
                        + "       join types t on t.id = a.type_id where a.id = ?",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(id);
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    AccidentType accidentType = new AccidentType();
                    accidentType.setId(rs.getInt("typeId"));
                    accidentType.setName(rs.getString("typeName"));
                    accident.setType(accidentType);
                    accident.setRules(Set.copyOf(findRulesByAccident(id)));
                    return accident;
                }, id));
    }

    public List<Accident> findAll() {
        return jdbc.query("select a.id  id, a.name name, a.text text, a.address address,"
                        + " t.id typeId, t.name typeName from accidents a "
                        + " join types t on a.type_id = t.id",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    AccidentType accidentType = new AccidentType();
                    accidentType.setId(rs.getInt("typeId"));
                    accidentType.setName(rs.getString("typeName"));
                    accident.setType(accidentType);
                    accident.setRules(Set.copyOf(findRulesByAccident(accident.getId())));
                    return accident;
                });
    }

    public Optional<AccidentType> findTypeById(int id) {
        AccidentType type = jdbc.queryForObject("select id, name from types where id = ?",
                (rs, row) -> new AccidentType(rs.getInt("id"),
                        rs.getString("name")), id);
        return Optional.ofNullable(type);
    }

    public List<AccidentType> findAllTypes() {
        return jdbc.query("select id, name from types",
                (rs, row) -> {
                    AccidentType type = new AccidentType();
                    type.setId(rs.getInt("id"));
                    type.setName(rs.getString("name"));
                    return type;
                });
    }

    public List<Rule> findAllRules() {
        return jdbc.query("select id, name from rules",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                });
    }

    public Set<Rule> findRulesByIds(String[] ids) {
        List<Integer> idList = new ArrayList<>();
        for (String id : ids) {
            idList.add(Integer.parseInt(id));
        }
        return new HashSet<>(jdbc.query("select id, name from rules where id in = ?",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                }, idList));
    }

    public List<Rule> findRulesByAccident(int id) {
        return jdbc.query("select r.id as id, r.name as name "
                        + " from accident_rules a join rules r on r.id = a.rule_id "
                        + " where accident_id = ?",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                },
                id);
    }
}