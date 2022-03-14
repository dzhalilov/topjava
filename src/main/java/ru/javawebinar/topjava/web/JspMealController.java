package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private final MealService mealService;

    public JspMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/meals")
    public String switcher(HttpServletRequest request) {
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete" -> {
                int id = getId(request);
                return delete(id, request);
            }
            case "create" -> {
                return create(request);
            }
            case "update" -> {
                return update(request);
            }
            case "filter" -> {
                return getWithFilter(request);
            }
        }
        return getAll(request);
    }

    @PostMapping("/meals")
    public void updating(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int userId = SecurityUtil.authUserId();
        if (!StringUtils.hasLength(request.getParameter("id"))) {
            checkNew(meal);
            log.info("create {} for user {}", meal, userId);
            mealService.create(meal, userId);
        } else {
            assureIdConsistent(meal, getId(request));
            log.info("update {} for user {}", meal, userId);
            mealService.update(meal, userId);
        }
        response.sendRedirect("meals");
    }

    public String create(HttpServletRequest request) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    public String update(HttpServletRequest request) {
        final Meal meal = get(getId(request));
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    public String delete(int id, HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal with id {}", id);
        mealService.delete(id, userId);
        request.setAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    public String getAll(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        request.setAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    public String getWithFilter(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(Objects.requireNonNullElse(request.getParameter("startDate"), "1900-01-01"));
        LocalDate endDate = parseLocalDate(Objects.requireNonNullElse(request.getParameter("endDate"), "3000-12-31"));
        LocalTime startTime = parseLocalTime(Objects.requireNonNullElse(request.getParameter("startTime"), "00:00"));
        LocalTime endTime = parseLocalTime(Objects.requireNonNullElse(request.getParameter("endTime"), "24:00"));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startDate, endDate, userId);
        request.setAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        return mealService.get(id, userId);
    }
}
