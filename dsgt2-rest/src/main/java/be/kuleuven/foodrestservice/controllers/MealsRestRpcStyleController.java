package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/cheapestMeal")
    Meal getCheapestMeal() {
        Optional<Meal> meal = mealsRepository.findCheapestMeal();

        return meal.orElseThrow();
    }

    @GetMapping("/restrpc/largestMeal")
    Meal getLargestMeal() {
        Optional<Meal> meal = mealsRepository.findLargestMeal();

        return meal.orElseThrow();
    }

    @PostMapping("/restrpc/addMeal")
    ResponseEntity<Void> addMeal(@RequestBody Meal meal){
        mealsRepository.addMeal(meal);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/restrpc/updateMeal")
    ResponseEntity<Void> updateMeal(@RequestBody Meal meal) {
        boolean updated = mealsRepository.updateMeal(meal);
        if(!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/restrpc/deleteMeal/{id}")
    ResponseEntity<Void> deleteMeal(@PathVariable String id) {
        boolean deleted = mealsRepository.deleteMeal(id);
        if(!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }
}

//curl -X POST http://localhost:8080/restrpc/addMeal         -H "Content-Type: application/json"         -d '{
//        "id": "new-meal-123",
//        "name": "Pasta",
//        "description": "Delicious pasta",
//        "mealType": "VEGAN",
//        "kcal": 600,
//        "price": 8.99
//        }'
//
//curl -X PUT http://localhost:8080/restrpc/updateMeal \
//        -H "Content-Type: application/json" \
//        -d '{
//        "id": "new-meal-123",
//        "name": "Updated Pasta",
//        "description": "More Delicious pasta",
//        "mealType": "VEGAN",
//        "kcal": 700,
//        "price": 9.99
//        }'
//
//curl -X DELETE http://localhost:8080/restrpc/deleteMeal/new-meal-123
//
//curl -v -X GET localhost:8080/rest/meals -H 'Content-type:application/json'
