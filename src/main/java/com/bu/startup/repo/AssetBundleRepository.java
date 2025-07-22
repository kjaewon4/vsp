package com.bu.startup.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bu.startup.entity.AssetBundleEntity;

@Repository
public interface AssetBundleRepository extends JpaRepository<AssetBundleEntity, Long> {
	
	 Optional<AssetBundleEntity> findByBundleName(String bundleName);
	 
}
