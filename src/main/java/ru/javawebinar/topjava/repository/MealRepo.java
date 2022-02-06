package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.List;

public class MealRepo implements MealStorageable {
    private List<Meal> meals;

    public MealRepo() {
        meals = Collections.synchronizedList(MealsUtil.getMeals());
    }

    public List<Meal> getMeals() {
        return meals;
    }
}
