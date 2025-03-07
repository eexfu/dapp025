package be.kuleuven.foodrestservice.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Order {
    private String address;
    private static final Map<String, Meal> meals = new HashMap<>();

    public Order(String address) {
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void addMeal(Meal meal) {
        meals.put(meal.id, meal);
    }

    public List<Meal> getMeal() {
        List<Meal> result = new ArrayList<>();
        for(Meal meal : meals.values()) {
            result.add(meal);
        }
        return result;
    }
}
