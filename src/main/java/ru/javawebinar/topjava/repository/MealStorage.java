package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {
    List<Meal> getAll();
    Meal findById(int id);
    void deleteById(int id);
    Meal add(Meal meal);
    Meal update(int id, Meal meal);
}
