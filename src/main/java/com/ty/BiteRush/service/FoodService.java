package com.ty.BiteRush.service;
import com.ty.BiteRush.entity.FoodItem;
import java.util.*;
public interface FoodService {
  List<FoodItem> listAll();
  FoodItem create(FoodItem item);
  void delete(Long id);
  Optional<FoodItem> findById(Long id);
}
