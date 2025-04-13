package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{id}")
    EntityModel<Meal> getMealById(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));

        return mealToEntityModel(id, meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    @GetMapping("/rest/cheapestMeal")
    EntityModel<Meal> getCheapestMeal() {
        Meal meal = mealsRepository.findCheapestMeal().orElseThrow();

        return mealToEntityModel(meal.getId(), meal);
    }

    @GetMapping("/rest/largestMeal")
    EntityModel<Meal> getLargestMeal() {
        Meal meal = mealsRepository.findLargestMeal().orElseThrow();

        return mealToEntityModel(meal.getId(), meal);
    }

    @PostMapping("/rest/addMeal")
    public ResponseEntity<EntityModel<Meal>> addMeal(@RequestBody Meal meal) {
        mealsRepository.addMeal(meal);
        return ResponseEntity.ok().body(mealToEntityModel(meal.getId(), meal));
    }

    @PutMapping("/rest/updateMeal")
    public ResponseEntity<EntityModel<Meal>> updateMeal(@RequestBody Meal meal) {
        boolean updated = mealsRepository.updateMeal(meal);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mealToEntityModel(meal.getId(), meal));
        }
        return ResponseEntity.ok().body(mealToEntityModel(meal.getId(), meal));
    }

    @DeleteMapping("/rest/deleteMeal/{id}")
    public ResponseEntity<EntityModel<Meal>> deleteMeal(@PathVariable String id) {
        boolean deleted = mealsRepository.deleteMeal(id);
        if (!deleted) {
            throw new MealNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("rest/addOrder/{address}")
    public ResponseEntity<EntityModel<Order>> addOrder(@PathVariable String address) {
        mealsRepository.addOrder(address);
        return ResponseEntity.ok().body(orderToEntityModel(address, null, mealsRepository.findOrder(address)));
    }

    @PutMapping("/rest/addMealToOrder/{address}")
    public ResponseEntity<EntityModel<Order>> addMealToOrder(@PathVariable String address, @RequestBody Meal meal) {
        mealsRepository.addMealToOrder(address, meal);
        return ResponseEntity.ok().body(orderToEntityModel(address, meal, mealsRepository.findOrder(address)));
    }

    @PutMapping("/rest/addOrderAndMeals/{address}")
    public ResponseEntity<EntityModel<Order>> addOrderAndMeals(@PathVariable String address, @RequestBody List<Meal> meals) {
        mealsRepository.addOrderAndMeals(address, meals);
        return ResponseEntity.ok().body(orderToEntityModel(address, null, mealsRepository.findOrder(address)));
    }

    @GetMapping("/rest/findOrder/{address}")
    public CollectionModel<EntityModel<Meal>> findOrder(@PathVariable String address){
        Order order = mealsRepository.findOrder(address);
        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : order.getMeal()) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).findOrder(address)).withSelfRel());
    }

    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"),
                linkTo(methodOn(MealsRestController.class).getCheapestMeal()).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getLargestMeal()).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).addMeal(meal)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).updateMeal(meal)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).deleteMeal(id)).withSelfRel());
    }

    private EntityModel<Order> orderToEntityModel(String address, Meal meal, Order order) {
        return EntityModel.of(order,
                linkTo(methodOn(MealsRestController.class).addOrder(address)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).addMealToOrder(address, meal)).withSelfRel());
    }
}