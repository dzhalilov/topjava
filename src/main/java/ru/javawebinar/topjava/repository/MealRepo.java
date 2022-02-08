package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepo implements MealStorageable {
    private static AtomicInteger id = new AtomicInteger(0);
    private List<Meal> meals = new CopyOnWriteArrayList<>();

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
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                meals.remove(i);
                break;
            }
        }
    }

    public void saveList() {
        for (Meal meal : MealsUtil.getMeals()) {
            meal.setId(id.incrementAndGet());
            meals.add(meal);
        }
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(id.incrementAndGet());
        meals.add(meal);
    }

    @Override
    public void editMeal(Meal meal) {
        int id = meal.getId();
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                meals.set(i, meal);
            }
        }
    }
}
