package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    List<Meal> findAllByUserId(int userId, Sort sort);

    @Modifying
    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.dateTime >= :startDateTime " +
            "AND m.dateTime < :endDateTime ORDER BY m.dateTime DESC")
    List<Meal> getBetweenHalfOpen(@Param("userId") int userId, @Param("startDateTime") LocalDateTime startDate, @Param("endDateTime") LocalDateTime endDate);
}
