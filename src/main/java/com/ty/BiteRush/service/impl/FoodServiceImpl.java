package com.ty.BiteRush.service.impl;

import com.ty.BiteRush.entity.FoodItem;
import com.ty.BiteRush.repository.FoodItemRepository;
import com.ty.BiteRush.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodItemRepository foodRepository;

    @Override
    public FoodItem create(FoodItem foodItem) {
        return foodRepository.save(foodItem);
    }

    @Override
    public List<FoodItem> listAll() {
        return foodRepository.findAll();
    }

    @Override
    public Optional<FoodItem> findById(Long id) {
        return foodRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
            foodRepository.flush(); // ✅ force DB synchronization
        }
    }

}
