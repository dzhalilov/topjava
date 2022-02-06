package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public class MealRepo {
    public List<Meal> meals;
    private static MealRepo mealRepo;

    private MealRepo() {
        meals = MealsUtil.getMeals();
    }
    public static synchronized MealRepo getInstance(){
        if (mealRepo == null){
            mealRepo = new MealRepo();
        }
        return mealRepo;
    }


}
