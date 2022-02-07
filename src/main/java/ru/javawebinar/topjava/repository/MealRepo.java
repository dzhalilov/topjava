package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepo implements MealStorageable {
    private static AtomicInteger id = new AtomicInteger(0);
    private List<Meal> meals;

    public MealRepo() {
    }

    public List<Meal> getMeals() {
        return meals;
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
        for (Meal meal : meals) {
            if (meal.getId() == id) {
                meals.remove(meal);
                break;
            }
        }
    }

    public static Integer getNextId() {
        return id.incrementAndGet();
    }

    public void saveList(){
        meals = Collections.synchronizedList(MealsUtil.getMeals());
    }
}
