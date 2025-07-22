package com.bu.startup.repo;

import com.bu.startup.entity.StartupItem;
import com.bu.startup.type.CategoryType;
import com.bu.startup.type.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartupItemRepository extends JpaRepository<StartupItem, Long> {

    List<StartupItem> findByCategory(CategoryType category);
    List<StartupItem> findByStatus(ItemStatus status);
    List<StartupItem> findByCategoryAndStatus(CategoryType category, ItemStatus status);
}