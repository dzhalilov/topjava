package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

public abstract class AbstractMealController {
    protected static final Logger log = LoggerFactory.getLogger(AbstractMealController.class);

    protected final MealService mealService;

    protected AbstractMealController(MealService mealService) {
        this.mealService = mealService;
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        return mealService.get(id, userId);
    }
}
