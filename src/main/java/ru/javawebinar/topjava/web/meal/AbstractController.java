package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.service.MealService;

public abstract class AbstractController {
    protected static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    protected final MealService mealService;

    public AbstractController(MealService mealService) {
        this.mealService = mealService;
    }
}
