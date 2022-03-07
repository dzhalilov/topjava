package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private final Sort SORT_DATETIME = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudMealRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(crudUserRepository.getById(userId));
            crudMealRepository.save(meal);
            return meal;
        }
        if (get(meal.getId(), userId) != null) {
            meal.setUser(crudUserRepository.getById(userId));
            return crudMealRepository.save(meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = get(id, userId);
        if (meal != null) {
            crudMealRepository.delete(meal);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudMealRepository.findById(id).orElse(null);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAllByUserId(userId, SORT_DATETIME);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.getBetweenHalfOpen(userId, startDateTime, endDateTime);
    }
}
