package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private final JdbcTemplate jdbcTemplate;
    RowMapper<Meal> rowMapper = new MealMapper();

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            jdbcTemplate.update("INSERT INTO meals (datetime, description, calories, userId) VALUES (?, ?, ?, ?)",
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
        return checkUserId(id, userId) && (meal != null) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE userId=? ORDER BY datetime DESC", new Object[]{userId}, rowMapper);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE userId=? " +
                        "AND datetime >= ? AND datetime <= ? ORDER BY datetime DESC",
                new Object[]{userId, startDateTime.toLocalDate(), endDateTime.toLocalDate()}, rowMapper);
    }

    private boolean checkUserId(int id, int userId) {
        Integer checkedUserId = jdbcTemplate.queryForObject("SELECT userId FROM meals WHERE id=?", new Object[]{id}, Integer.class);
        return (checkedUserId != null) && (userId == checkedUserId);
    }
}
