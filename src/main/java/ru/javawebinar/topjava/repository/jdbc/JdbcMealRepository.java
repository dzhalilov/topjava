package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private final JdbcTemplate jdbcTemplate;
    private RowMapper<Meal> rowMapper = new BeanPropertyRowMapper<>(Meal.class);

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            jdbcTemplate.update("INSERT INTO meals (date_time, description, calories, user_id) VALUES (?, ?, ?, ?)",
                    new Object[]{meal.getDateTime(), meal.getDescription(), meal.getCalories(), userId});
        } else {
            if (!checkUserId(meal.getId(), userId)) return null;
            jdbcTemplate.update("UPDATE meals SET date_time=?, description=?, calories=? WHERE id=?", new Object[]{
                    meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getId()
            });
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (!checkUserId(id, userId)) return false;
        return jdbcTemplate.update("DELETE FROM meals WHERE id=?", id) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM meals WHERE id=?", new Object[]{id}, rowMapper));
        return checkUserId(id, userId) && (meal != null) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", new Object[]{userId}, rowMapper);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? " +
                        "AND ? <= date_time AND date_time < ? ORDER BY date_time DESC",
                new Object[]{userId, startDateTime, endDateTime}, rowMapper);
    }

    private boolean checkUserId(int id, int userId) {
        Integer checkedUserId = jdbcTemplate.queryForObject("SELECT user_id FROM meals WHERE id=?", Integer.class, id);
        return (checkedUserId != null) && (userId == checkedUserId);
    }
}
