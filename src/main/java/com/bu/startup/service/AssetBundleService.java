package com.bu.startup.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.bu.startup.entity.User;
import com.bu.startup.type.CategoryType;
import com.bu.startup.type.ItemStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bu.startup.entity.AssetBundleEntity;
import com.bu.startup.repo.AssetBundleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetBundleService {

    private final AssetBundleRepository assetBundleRepository;
    private final UserService userService; // UserService 주입

    @Value("${upload.path}")
    private String uploadPath;

    // 파일 저장 헬퍼 메서드
    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath, fileName);
        Files.createDirectories(path.getParent()); // 디렉토리가 없으면 생성
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return path.toString();
    }

    // 포괄적인 saveAssetBundle 메서드
    public AssetBundleEntity saveAssetBundle(
            String bundleName,
            String bundleTitle,
            String bundleDescription,
            MultipartFile thumbnailFile,
            MultipartFile winFile,
            MultipartFile androidFile,
            CategoryType category,
            String detailDescription,
            User author,
            boolean isAdmin) throws IOException {

        String thumbnailPath = saveFile(thumbnailFile);

        List<String> filePaths = new ArrayList<>();
        String winFilePath = saveFile(winFile);
        if (winFilePath != null) {
            filePaths.add(winFilePath);
        }
        String androidFilePath = saveFile(androidFile);
        if (androidFilePath != null) {
            filePaths.add(androidFilePath);
        }

        AssetBundleEntity bundle = AssetBundleEntity.builder()
                .bundleName(bundleName)
                .bundleTitle(bundleTitle)
                .bundleDescription(bundleDescription)
                .thumbnailPath(thumbnailPath)
                .filePath(filePaths)
                .category(category)
                .detailDescription(detailDescription)
                .author(author)
                .views(0) // 초기 조회수 0
                .status(isAdmin ? ItemStatus.APPROVED : ItemStatus.PENDING) // 권한에 따라 상태 설정
                .build();

        return assetBundleRepository.save(bundle);
    }

    public List<AssetBundleEntity> getAllBundles() {
        return assetBundleRepository.findAll();
    }

    public Optional<AssetBundleEntity> getBundleById(Long id) {
        return assetBundleRepository.findById(id);
    }

    public List<AssetBundleEntity> getBundleByIds(List<Long> id) {
        return assetBundleRepository.findAllById(id);
    }

    public byte[] loadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    public void deleteByIds(List<Long> ids) {
        List<AssetBundleEntity> bundles = assetBundleRepository.findAllById(ids);

        for (AssetBundleEntity bundle : bundles) {
            // 실제 파일 삭제 (썸네일)
            deletePhysicalFile(bundle.getThumbnailPath());
            // 실제 파일 삭제 (파일 경로 리스트)
            if (bundle.getFilePath() != null) {
                for (String str : bundle.getFilePath()) {
                    deletePhysicalFile(str);
                }
            }
            // DB에서 삭제
            assetBundleRepository.delete(bundle);
        }
    }

    private void deletePhysicalFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    System.err.println("파일 삭제 실패: " + filePath);
                }
            }
        }
    }

    /**
     * 필터 검색
     * @param category
     * @return
     */
    public List<AssetBundleEntity> getBundlesByCategory(CategoryType category) {
        return assetBundleRepository.findByCategory(category);
    }

    public List<AssetBundleEntity> getBundlesByStatus(ItemStatus status) {
        return assetBundleRepository.findByStatus(status);
    }

    public List<AssetBundleEntity> getPendingBundles() {
        return assetBundleRepository.findByStatus(ItemStatus.PENDING);
    }

    @Transactional
    public AssetBundleEntity updateBundleStatus(Long bundleId, ItemStatus newStatus) {
        AssetBundleEntity bundle = assetBundleRepository.findById(bundleId)
                .orElseThrow(() -> new IllegalArgumentException("AssetBundle not found"));
        bundle.setStatus(newStatus);
        return assetBundleRepository.save(bundle);
    }

    // 여러 상태로 조회
    public List<AssetBundleEntity> getBundlesByCategoryAndStatuses(CategoryType category, List<ItemStatus> statuses) {
        if (statuses.isEmpty()) return Collections.emptyList();
        return assetBundleRepository.findByCategoryAndStatusIn(category, statuses);
    }

    // status 컬럼의 값이 주어진 리스트에 포함된 값 중 하나인 데이터만 조회
    public List<AssetBundleEntity> getBundlesByStatuses(List<ItemStatus> statuses) {
        if (statuses.isEmpty()) return Collections.emptyList();
        return assetBundleRepository.findByStatusIn(statuses);
    }

}