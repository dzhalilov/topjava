package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorageable {
    List<Meal> getMeals();
    Meal findById(int id);
    int findId(Meal meal);
    void deleteById(int id);
}
