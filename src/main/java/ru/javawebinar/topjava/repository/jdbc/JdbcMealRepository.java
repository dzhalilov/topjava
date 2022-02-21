package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private RowMapper<Meal> rowMapper = new BeanPropertyRowMapper<>(Meal.class);

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            MapSqlParameterSource map = new MapSqlParameterSource()
                    .addValue("id", meal.getId())
                    .addValue("date_time", meal.getDateTime())
                    .addValue("description", meal.getDescription())
                    .addValue("calories", meal.getCalories())
                    .addValue("user_id", userId);
            int newId = (int) simpleJdbcInsert.executeAndReturnKey(map);
            meal.setId(newId);
        } else {
            if (jdbcTemplate.update("UPDATE meals SET date_time=?, description=?, calories=? WHERE id=? AND user_id=?",
                    new Object[]{meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getId(), userId}) == 0)
                return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?", rowMapper, id, userId));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", rowMapper, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? AND ? <= date_time AND date_time < ? " +
                "ORDER BY date_time DESC", rowMapper, userId, startDateTime, endDateTime);
    }
}
