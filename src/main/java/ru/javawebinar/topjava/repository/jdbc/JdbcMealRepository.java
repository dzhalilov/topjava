package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    RowMapper<Meal> rowMapper = new MealMapper();
    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
//        MapSqlParameterSource map = new MapSqlParameterSource()
//                .addValue("id", meal.getId())
//                .addValue("dateTime", meal.getDateTime())
//                .addValue("description", meal.getDescription())
//                .addValue("calories", meal.getCalories())
//                .addValue("userId", userId);
        if (meal.isNew()) {
//            Number newKey = insertUser.executeAndReturnKey(map);
//            meal.setId(newKey.intValue());
            jdbcTemplate.update("INSERT INTO meals (datetime, description, calories, user_id) VALUES (?, ?, ?, ?)",
                    new Object[]{meal.getDateTime(), meal.getDescription(), meal.getCalories(), userId});
        } else {
            if (!checkUserId(meal.getId(), userId)) return null;
            jdbcTemplate.update("UPDATE meals SET dateTime=?, description=?, calories=? WHERE id=?", new Object[]{
                    meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getId()
            });
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (!checkUserId(id, userId)) return false;
        jdbcTemplate.update("DELETE FROM meals WHERE id=?", id);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = jdbcTemplate.query("SELECT * FROM meals WHERE id=?", new Object[]{id}, rowMapper)
                .stream().findAny().orElse(null);
        return meal == null && checkUserId(id, userId) ? null : meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY datetime DESC", new Object[]{userId}, rowMapper);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? " +
                        "AND datetime >= ? AND datetime <= ? ORDER BY datetime DESC",
                new Object[]{userId, startDateTime.toLocalDate(), endDateTime.toLocalDate()}, rowMapper);
    }

    private boolean checkUserId(int id, int userId) {
        int checkedUserId = jdbcTemplate.queryForObject("SELECT user_id FROM meals WHERE id=?", new Object[]{id}, Integer.class);
        return checkedUserId == userId;
    }
}
