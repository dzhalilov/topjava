package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> mealsForUser1 = Arrays.asList(
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 10, 0), "Завтрак User1", 500),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 13, 0), "Обед User1", 1000),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 20, 0), "Ужин User1", 500),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0), "Еда на граничное значение User1", 100),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 10, 0), "Завтрак User1", 1000),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 13, 0), "Обед User1", 500),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 20, 0), "Ужин User1", 410)
    );
    public static final List<Meal> mealsForUser2 = Arrays.asList(
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 9, 0), "Завтрак User2", 200),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 13, 30), "Обед User2", 1500),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 21, 0), "Ужин User2", 600),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0), "Еда на граничное значение User2", 150),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 11, 0), "Завтрак User2", 800),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 15, 0), "Обед User2", 750),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 21, 0), "Ужин User2", 300)
    );

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
