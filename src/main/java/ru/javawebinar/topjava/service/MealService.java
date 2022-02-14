package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    @Autowired
    private MealRepository repository;

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<MealTo> getAll(int userId, int calories) {
        return MealsUtil.getTos(repository.getAll(userId), calories);
    }

    public Meal update(Meal meal, int userId) {
        meal.setUserId(userId);
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int userId, int calories) {
        Predicate<Meal> datePredicate1 = meal -> !meal.getDate().isBefore(startDate);
        Predicate<Meal> datePredicate2 = meal -> !meal.getDate().isAfter(endDate);
        Predicate<Meal> datePredicate = datePredicate1.and(datePredicate2);
        return MealsUtil.getFilteredTos(repository.getFiltered(datePredicate, userId),
                calories, startTime, endTime);
    }
}