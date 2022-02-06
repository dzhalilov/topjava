package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.List;

public class MealRepo {
    private List<Meal> meals;
    private static MealRepo mealRepo;

    private MealRepo() {
        meals = Collections.synchronizedList(MealsUtil.getMeals());
    }
    public static synchronized MealRepo getInstance(){
        if (mealRepo == null){
            mealRepo = new MealRepo();
        }
        return mealRepo;
    }

    public List<Meal> getMeals() {
        return meals;
    }
}
