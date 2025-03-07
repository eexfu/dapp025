package be.kuleuven.foodrestservice.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class MealsRepository {
    // map: id -> meal
    private static final Map<String, Meal> meals = new HashMap<>();
    private static final Map<String, Order> orders = new HashMap<>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setId("5268203c-de76-4921-a3e3-439db69c462a");
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealType(MealType.MEAT);
        a.setKcal(1100);
        a.setPrice((10.00));

        meals.put(a.getId(), a);

        Meal b = new Meal();
        b.setId("4237681a-441f-47fc-a747-8e0169bacea1");
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealType(MealType.VEGAN);
        b.setKcal(637);
        b.setPrice((7.00));

        meals.put(b.getId(), b);

        Meal c = new Meal();
        c.setId("cfd1601f-29a0-485d-8d21-7607ec0340c8");
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealType(MealType.FISH);
        c.setKcal(950);
        c.setPrice(5.00);

        meals.put(c.getId(), c);
    }

    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
        return Optional.ofNullable(meal);
    }

    public Collection<Meal> getAllMeal() {
        return meals.values();
    }

    public Optional<Meal> findCheapestMeal() {
        return meals.values().stream().min(Comparator.comparingDouble(Meal::getPrice));
    }

    public Optional<Meal> findLargestMeal() {
        return meals.values().stream().max(Comparator.comparingDouble(Meal::getKcal));
    }

    public void addMeal(Meal meal) {
        Assert.notNull(meal, "The meal must not be null");
        meals.put(meal.id, meal);
    }

    public boolean updateMeal(Meal meal) {
        Assert.notNull(meal, "The meal must not be null");
        if(!meals.containsKey(meal.id)){
            return false;
        }
        else {
            meals.replace(meal.id, meal);
        }
        return true;
    }

    public boolean deleteMeal(String id) {
        Assert.notNull(id, "The meal ID must not be null");
        return meals.remove(id) != null;
    }

    public void addOrder(String address) {
        Assert.notNull(address, "The address must not be null");
        orders.put(address, new Order(address));
    }

    public void addMealToOrder(String address, Meal meal){
        Assert.notNull(address, "The address must not be null");
        orders.get(address).addMeal(meal);
    }

    public Order findOrder(String address) {
        Assert.notNull(address, "The address must not be null");
        return orders.get(address);
    }

    public void addOrderAndMeals(String address, List<Meal> meals) {
        Assert.notNull(address, "The address must not be null");
        Assert.notNull(meals, "The meals must not be null");
        orders.put(address, new Order(address));
        for(Meal meal:meals){
            orders.get(address).addMeal(meal);
        }
    }
}
