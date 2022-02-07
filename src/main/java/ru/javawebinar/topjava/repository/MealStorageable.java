package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorageable {
    List<Meal> getMeals();
    Meal findById(int id);
    void deleteById(int id);
    void saveList();
    void addMeal(Meal meal);
}
