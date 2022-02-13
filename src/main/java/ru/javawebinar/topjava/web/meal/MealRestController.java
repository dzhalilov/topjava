package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public List<MealTo> getAll() {
        log.info("getAll");
        return service.getAll(authUserId());
    }

    public List<MealTo> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll");
        return service.getAll(startDate, endDate, startTime, endTime, authUserId());
    }

    public Meal update(Meal meal) {
        log.info("update {}", meal);
        return service.update(meal, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        return service.update(meal, authUserId());
    }

    public Meal get(int id) {
        log.info("get with id: {}", id);
        return service.get(id, authUserId());
    }

    public void delete(int id) {
        log.info("delete with id: {}", id);
        service.delete(id, authUserId());
    }

}