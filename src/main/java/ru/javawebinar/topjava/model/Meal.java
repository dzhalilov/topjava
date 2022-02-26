package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.ALL_FILTERED, query = "FROM Meal WHERE user.id=:userId AND :startTime <= dateTime " +
                "AND dateTime < :endTime ORDER BY dateTime desc "),
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal WHERE id=:id AND user.id=:userId"),
        @NamedQuery(name = Meal.GET, query = "FROM Meal WHERE id=:id AND user.id=:userId"),
        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal SET user.id=:userId, dateTime=:dateTime, description=:description, calories=:calories " +
                "WHERE user.id=:userId AND id=:id")
})
@Entity
@Table(name = "meals")
public class Meal extends AbstractBaseEntity {
    public static final String ALL_FILTERED = "Meal.allFiltered";
    public static final String DELETE = "Meal.delete";
    public static final String GET = "Meal.get";
    public static final String UPDATE = "Meal.update";

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp = now()")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 3, max = 128)
    private String description;

    @Column(name = "calories", nullable = false)
    @NotNull
    @Min(value = 1, message = "Calories can't be less then 1")
    @Max(value = 15000, message = "Human wouldn't eat more than 1500 calories")
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
