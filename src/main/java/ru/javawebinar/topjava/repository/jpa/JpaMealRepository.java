package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User takenUser = em.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(takenUser);
            em.persist(meal);
            return meal;
        } else {
            if (em.createNamedQuery(Meal.UPDATE)
                    .setParameter("userId", userId)
                    .setParameter("id", meal.getId())
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories())
                    .executeUpdate() != 0) {
                meal.setUser(takenUser);
                return meal;
            } else {
                return null;
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        try {
            return em.createNamedQuery(Meal.DELETE)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .executeUpdate() != 0;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        try {
            return em.createNamedQuery(Meal.GET, Meal.class)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getBetweenHalfOpen(LocalDateTime.of(1970, 1, 1, 0, 0),
                LocalDateTime.of(2300, 12, 31, 23, 59), userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.ALL_FILTERED, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startTime", startDateTime)
                .setParameter("endTime", endDateTime)
                .getResultList();
    }
}