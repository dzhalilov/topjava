package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final Map<Integer, List<Integer>> usersMealId = new ConcurrentHashMap<>();
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
            repository.put(meal.getId(), meal);
            usersMealId.putIfAbsent(userId, new CopyOnWriteArrayList<>());
            usersMealId.get(userId).add(meal.getId());
            return meal;
        }
        return get(meal.getId(), userId) != null ?
                repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (get(id, userId) != null && repository.remove(id) != null) {
            usersMealId.get(userId).remove(Integer.valueOf(id));
            return true;
        } else return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getFiltered(Predicate<Meal> datePredicate, int userId) {
        return usersMealId.getOrDefault(userId, new ArrayList<>()).stream()
                .map(repository::get)
                .filter(datePredicate)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFiltered(date -> true, userId);
    }
}

