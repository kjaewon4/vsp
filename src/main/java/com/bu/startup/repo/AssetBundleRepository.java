package com.bu.startup.repo;

import java.util.List;
import java.util.Optional;

import com.bu.startup.entity.User;
import com.bu.startup.type.CategoryType;
import com.bu.startup.type.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bu.startup.entity.AssetBundleEntity;

@Repository
public interface AssetBundleRepository extends JpaRepository<AssetBundleEntity, Long> {
	
	 Optional<AssetBundleEntity> findByBundleName(String bundleName);

    List<AssetBundleEntity> findByCategory(CategoryType category);
    List<AssetBundleEntity> findByStatus(ItemStatus status);

    List<AssetBundleEntity> findByCategoryAndStatusIn(CategoryType category, List<ItemStatus> statuses);

    List<AssetBundleEntity> findByStatusIn(List<ItemStatus> statuses);
    List<AssetBundleEntity> findByAuthor(User author);
}
