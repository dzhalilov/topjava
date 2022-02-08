package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepo;
import ru.javawebinar.topjava.repository.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealStorage repo;

    @Override
    public void init() {
        repo = new MealRepo();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        log.debug("forward to meals");
        String action = req.getParameter("action") != null ? req.getParameter("action") : "";
        switch (action) {
            case "delete":
                log.debug("deleted id: {}", id);
                repo.deleteById(Integer.parseInt(id));
                break;
            case "edit": {
                log.debug("start edit: {}", id);
                Meal meal = repo.findById(Integer.parseInt(id));
                req.setAttribute("meal", meal);
                req.setAttribute("act", "Edit");
                req.getRequestDispatcher("meal.jsp").forward(req, resp);
                break;
            }
            case "add": {
                log.debug("start adding Meal");
                Meal meal = new Meal();
                req.setAttribute("meal", meal);
                req.setAttribute("act", "Add");
                req.getRequestDispatcher("meal.jsp").forward(req, resp);
                break;
            }
        }
        List<MealTo> list = MealsUtil.filteredByStreams(repo.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
        req.setAttribute("mealsTo", list);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("doPost Method");

        String stringDateTime = req.getParameter("dateTime");
        LocalDateTime dateTime = LocalDateTime.parse(stringDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String id = req.getParameter("id");
        Meal meal = new Meal(dateTime, description, calories);
        if (id != null && !id.isEmpty()) {
            repo.update(Integer.parseInt(id), meal);
        } else {
            repo.add(meal);
        }
        resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        resp.setHeader("Location", "meals");
        resp.sendRedirect("meals");
    }
}
