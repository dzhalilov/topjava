package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepo implements MealStorage {
    private AtomicInteger count = new AtomicInteger(0);
    private ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap();

    public MealRepo() {
        for (Meal meal : MealsUtil.getMeals()) {
            add(meal);
        }
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal findById(int id) {
        return meals.get(id);
    }

    @Override
    public void deleteById(int id) {
        meals.remove(id);
    }

    @Override
    public Meal add(Meal meal) {
        Integer id = count.incrementAndGet();
        meal.setId(id);
        meals.put(id, meal);
        return meal;
    }

    @Override
    public Meal update(int id, Meal meal) {
        meal.setId(id);
        return meals.put(id, meal);
    }
}
