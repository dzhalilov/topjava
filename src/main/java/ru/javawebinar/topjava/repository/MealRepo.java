package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepo implements MealStorage {
    private AtomicInteger count = new AtomicInteger(0);
    private List<Meal> meals = new CopyOnWriteArrayList<>();

    public MealRepo() {
        for (Meal meal : MealsUtil.getMeals()) {
            add(meal);
        }
    }

    public List<Meal> getAll() {
        return new ArrayList<>(meals);
    }

    @Override
    public Meal findById(int id) {
        for (Meal meal : meals) {
            if (meal.getId() == id) return meal;
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                meals.remove(i);
                break;
            }
        }
    }

    @Override
    public Meal add(Meal meal) {
        meal.setId(count.incrementAndGet());
        meals.add(meal);
        return meal;
    }

    @Override
    public Meal update(int id, Meal meal) {
        meal.setId(id);
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                meals.set(i, meal);
                return meal;
            }
        }
        return null;
    }
}
