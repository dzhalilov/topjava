package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepo implements MealStorageable {
    private AtomicInteger id = new AtomicInteger(0);
    private ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap();
    ;

    public MealRepo() {
        for (Meal meal : MealsUtil.getMeals()) {
            meals.put(id.incrementAndGet(), meal);
        }
    }

    public List<Meal> getMeals() {
        List<Meal> list = new ArrayList<>();
        for (Map.Entry<Integer, Meal> pair : meals.entrySet()) {
            list.add(pair.getValue());
        }
        return list;
    }

    @Override
    public Meal findById(int id) {
        return meals.get(id);
    }

    @Override
    public int findId(Meal meal) {
        for (Map.Entry<Integer, Meal> pair : meals.entrySet()) {
            if (pair.getValue() == meal) return pair.getKey();
        }
        return -1;
    }

    @Override
    public void deleteById(int id) {
        meals.remove(id);
    }
}
