package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private volatile Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userID) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userID);
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (meal.getUserId() != userID) return null;
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.get(id).getUserId() != userId) return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(id).getUserId() == userId ? repository.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(meal -> !meal.getDateTime().toLocalDate().isBefore(startDate) && !meal.getDateTime().toLocalDate().isAfter(endDate))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public int getUserId(Meal meal) {
        return repository.get(meal.getId()).getUserId();
    }
}
