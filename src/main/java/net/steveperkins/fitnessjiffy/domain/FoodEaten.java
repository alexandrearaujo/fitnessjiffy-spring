package net.steveperkins.fitnessjiffy.domain;

import com.google.common.base.Optional;

import java.sql.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
        name = "FOOD_EATEN",
        uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "FOOD_ID", "DATE"})
)
public final class FoodEaten {

    @Id
    @Column(name = "ID", columnDefinition = "BYTEA", length = 16)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "FOOD_ID", nullable = false)
    private Food food;

    @Column(name = "DATE", nullable = false)
    private Date date;

    @Column(name = "SERVING_TYPE", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Food.ServingType servingType;

    @Column(name = "SERVING_QTY", nullable = false)
    private Double servingQty;

    public FoodEaten(
            @Nullable final UUID id,
            @Nonnull final User user,
            @Nonnull final Food food,
            @Nonnull final Date date,
            @Nonnull final Food.ServingType servingType,
            final double servingQty
    ) {
        this.id = Optional.fromNullable(id).or(UUID.randomUUID());
        this.user = user;
        this.food = food;
        this.date = (Date) date.clone();
        this.servingType = servingType;
        this.servingQty = servingQty;
    }

    public FoodEaten() {
    }

    @Nonnull
    public UUID getId() {
        return id;
    }

    public void setId(@Nonnull final UUID id) {
        this.id = id;
    }

    @Nonnull
    public User getUser() {
        return user;
    }

    public void setUser(@Nonnull final User user) {
        this.user = user;
    }

    @Nonnull
    public Food getFood() {
        return food;
    }

    public void setFood(@Nonnull final Food food) {
        this.food = food;
    }

    @Nonnull
    public Date getDate() {
        return (Date) date.clone();
    }

    public void setDate(@Nonnull final Date date) {
        this.date = (Date) date.clone();
    }

    @Nonnull
    public Food.ServingType getServingType() {
        return servingType;
    }

    public void setServingType(@Nonnull final Food.ServingType servingType) {
        this.servingType = servingType;
    }

    @Nonnull
    public Double getServingQty() {
        return servingQty;
    }

    public void setServingQty(@Nonnull final Double servingQty) {
        this.servingQty = servingQty;
    }

    public int getCalories() {
        return (int) (food.getCalories() * getRatio());
    }

    public double getFat() {
        return food.getCalories() * getRatio();
    }

    public double getSaturatedFat() {
        return food.getSaturatedFat() * getRatio();
    }

    public double getSodium() {
        return food.getSodium() * getRatio();
    }

    public double getCarbs() {
        return food.getCarbs() * getRatio();
    }

    public double getFiber() {
        return food.getFiber() * getRatio();
    }

    public double getSugar() {
        return food.getSugar() * getRatio();
    }

    public double getProtein() {
        return food.getProtein() * getRatio();
    }

    public double getPoints() {
        return food.getPoints() * getRatio();
    }

    private double getRatio() {
        double ratio;
        if (servingType.equals(food.getDefaultServingType())) {
            // Default serving type was used
            ratio = servingQty / food.getServingTypeQty();
        } else {
            // Serving type needs conversion
            final double ouncesInThisServingType = servingType.getValue();
            final double ouncesInDefaultServingType = food.getDefaultServingType().getValue();
            ratio = (ouncesInDefaultServingType * food.getServingTypeQty() == 0) ? 0 : (ouncesInThisServingType * servingQty) / (ouncesInDefaultServingType * food.getServingTypeQty());
        }
        return ratio;
    }
}
