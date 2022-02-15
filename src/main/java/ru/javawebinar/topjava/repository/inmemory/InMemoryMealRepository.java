package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repo = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.mealsForUser1.forEach(meal -> this.save(meal, 1));
        MealsUtil.mealsForUser2.forEach(meal -> this.save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repo.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
            repo.get(userId).put(meal.getId(), meal);
            return meal;
        }
        return get(meal.getId(), userId) != null ?
                repo.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return get(id, userId) != null && repo.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repo.getOrDefault(userId, new ConcurrentHashMap<>()).getOrDefault(id, null);
        return meal != null && meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        return repo.getOrDefault(userId, new ConcurrentHashMap<>())
                .values()
                .stream()
                .filter(meal -> !meal.getDate().isBefore(startDate) && !meal.getDate().isAfter(endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFiltered(LocalDate.MIN, LocalDate.MAX, userId);
    }
}

