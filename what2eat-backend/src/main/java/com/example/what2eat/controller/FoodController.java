package com.example.what2eat.controller;

import com.example.what2eat.model.Food;
import com.example.what2eat.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    public List<Food> getAllFood() {
        return foodRepository.findAll();
    }

    @PostMapping
    public Food addFood(@RequestBody Food food) {
        return foodRepository.save(food);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodRepository.deleteById(id);
    }

    @GetMapping("/random")
    public Food getRandomFood() {
        List<Food> foodList = foodRepository.findAll();
        if (foodList.isEmpty()) {
            return null;
        }
        return foodList.get(new Random().nextInt(foodList.size()));
    }
}
