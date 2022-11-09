package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;
    private static final String INSERT_ACCIDENTS_QUERY =
            "INSERT INTO accidents (name, text, address, type_id) "
                    + "VALUES (?, ?, ?, ?)";
    private static final String INSERT_ACCIDENT_RULES_QUERY =
            "INSERT INTO accident_rules (accident_id, rule_id) VALUES (?, ?)";
    private static final String UPDATE_ACCIDENTS_QUERY =
            "UPDATE accidents SET name = ?, text = ?, address = ?, type_id = ? "
                    + "WHERE id = ?";
    private static final String SELECT_ACCIDENTS_QUERY =
            "SELECT a.name name, a.text text, a.address address, "
                    + "t.id typeId, t.name typeName FROM accidents a "
                    + "JOIN types t ON t.id = a.type_id WHERE a.id = ?";
    private static final String SELECT_ALL_ACCIDENTS_QUERY =
            "SELECT a.id  id, a.name name, a.text text, a.address address,"
                    + " t.id typeId, t.name typeName FROM accidents a"
                    + " JOIN types t ON a.type_id = t.id";
    private static final String DELETE_ACCIDENT_RULES_QUERY =
            "DELETE FROM accident_rules WHERE accident_id = ?";
    private static final String SELECT_TYPE_QUERY =
            "SELECT id, name FROM types WHERE id = ?";

    private static final String SELECT_ALL_TYPES_QUERY = "SELECT id, name FROM types";

    private static final String SELECT_ALL_RULES_QUERY = "SELECT id, name FROM rules";
    private static final String SELECT_RULES_BY_ACCIDENT_QUERY =
            "SELECT r.id id, r.name name FROM accident_rules a JOIN rules r ON r.id = a.rule_id"
                    + " WHERE accident_id = ?";

    public Accident add(Accident accident, String[] rIds) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement statement =
                    con.prepareStatement(INSERT_ACCIDENTS_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, accident.getName());
            statement.setString(2, accident.getText());
            statement.setString(3, accident.getAddress());
            statement.setInt(4, accident.getType().getId());
            return statement;
        }, keyHolder);
        Integer id = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        for (String rId : rIds) {
            jdbc.update(
                    INSERT_ACCIDENT_RULES_QUERY,
                    id, Integer.parseInt(rId));
        }
        return accident;
    }

    public Accident update(Accident accident, String[] rIds) {
        jdbc.update(UPDATE_ACCIDENTS_QUERY,
                accident.getName(), accident.getText(), accident.getAddress(),
                accident.getType().getId(), accident.getId());
        jdbc.update(DELETE_ACCIDENT_RULES_QUERY,
                accident.getId());
        for (String rId : rIds) {
            jdbc.update(INSERT_ACCIDENT_RULES_QUERY,
                    accident.getId(), Integer.parseInt(rId));
        }
        return accident;
    }

    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(jdbc.queryForObject(SELECT_ACCIDENTS_QUERY,
                new AccidentMapper(id), id));
    }

    public List<Accident> findAll() {
        return jdbc.query(SELECT_ALL_ACCIDENTS_QUERY,
                new AccidentMapper());
    }

    public Optional<AccidentType> findTypeById(int id) {
        AccidentType type = jdbc.queryForObject(SELECT_TYPE_QUERY,
                (rs, row) -> new AccidentType(rs.getInt("id"),
                        rs.getString("name")), id);
        return Optional.ofNullable(type);
    }

    public List<AccidentType> findAllTypes() {
        return jdbc.query(SELECT_ALL_TYPES_QUERY,
                (rs, row) -> {
                    AccidentType type = new AccidentType();
                    type.setId(rs.getInt("id"));
                    type.setName(rs.getString("name"));
                    return type;
                });
    }

    public List<Rule> findAllRules() {
        return jdbc.query(SELECT_ALL_RULES_QUERY,
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                });
    }

    public List<Rule> findRulesByAccident(int id) {
        return jdbc.query(SELECT_RULES_BY_ACCIDENT_QUERY,
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                },
                id);
    }

    private class AccidentMapper implements RowMapper<Accident> {
        private int id = -1;

        public AccidentMapper() {
        }

        public AccidentMapper(int id) {
            this.id = id;
        }

        public Accident mapRow(ResultSet rs, int rowNum) throws SQLException {
            Accident accident = new Accident();
            if (id == -1) {
                accident.setId(rs.getInt("id"));
            } else {
                accident.setId(id);
            }
            accident.setName(rs.getString("name"));
            accident.setText(rs.getString("text"));
            accident.setAddress(rs.getString("address"));
            AccidentType accidentType = new AccidentType();
            accidentType.setId(rs.getInt("typeId"));
            accidentType.setName(rs.getString("typeName"));
            accident.setType(accidentType);
            accident.setRules(Set.copyOf(findRulesByAccident(accident.getId())));
            return accident;
        }
    }
}