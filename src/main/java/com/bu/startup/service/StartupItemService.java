package com.bu.startup.service;

import com.bu.startup.entity.StartupItem;
import com.bu.startup.repo.StartupItemRepository;
import com.bu.startup.type.CategoryType;
import com.bu.startup.type.ItemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StartupItemService {
    private final StartupItemRepository startupItemRepository;

    public List<StartupItem> getAllItems() {
        return startupItemRepository.findAll();
    }

    public List<StartupItem> getItemsByCategory(CategoryType category) {
        return startupItemRepository.findByCategory(category);
    }

    public List<StartupItem> getItemsByStatus(ItemStatus status) {
        return startupItemRepository.findByStatus(status);
    }

    public List<StartupItem> getItemsByCategoryAndStatus(CategoryType category, ItemStatus status) {
        return startupItemRepository.findByCategoryAndStatus(category, status);
    }

}
