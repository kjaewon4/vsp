package com.bu.startup.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Value("${upload.path}")
    private String uploadPath;

    public AssetBundleEntity saveAssetBundle(
    		MultipartFile file, 
    		String bundleName) throws IOException {
        // 저장 경로 구성
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();      
        Path pathW = Paths.get(uploadPath, fileName);

        // 파일 저장
        Files.createDirectories(pathW.getParent());
        Files.copy(file.getInputStream(), pathW, StandardCopyOption.REPLACE_EXISTING);

        // DB 저장
        AssetBundleEntity bundle = AssetBundleEntity.builder()
                .bundleName(bundleName)
                .filePath(new String[]{pathW.toString() , ""})
                .uploadedAt(LocalDateTime.now())
                .build();

        return assetBundleRepository.save(bundle);
    }
    
    public AssetBundleEntity saveAssetBundle(
    		MultipartFile file, 
    		String bundleName, 
    		String bundleTitle, 
    		String bundleDescription) throws IOException {
        // 저장 경로 구성
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();      
        Path pathW = Paths.get(uploadPath, fileName);

        // 파일 저장
        Files.createDirectories(pathW);
//        Files.createDirectories(pathW.getParent());
        Files.copy(file.getInputStream(), pathW, StandardCopyOption.REPLACE_EXISTING);

        // DB 저장
        AssetBundleEntity bundle = AssetBundleEntity.builder()
                .bundleName(bundleName)
                .bundleTitle(bundleTitle)
                .bundleDescription(bundleDescription)
                .filePath(  new String[]{pathW.toString() , ""} )
                .uploadedAt(LocalDateTime.now())
                .build();
        
        return assetBundleRepository.save(bundle);
    }
    
    public AssetBundleEntity saveAssetBundle(
    		MultipartFile win, 
    		MultipartFile and, 
    		String bundleName, 
    		String bundleTitle, 
    		String bundleDescription) throws IOException {
        // 저장 경로 구성
        String fileName = System.currentTimeMillis() + "_" + win.getOriginalFilename() + "_w";      
        Path pathW = Paths.get(uploadPath, fileName);

        // 파일 저장
        Files.createDirectories(pathW.getParent());
        Files.copy(win.getInputStream(), pathW, StandardCopyOption.REPLACE_EXISTING);
        /////////////////////////////////////////////////////////////////////////////////////////////
        ///
        fileName = System.currentTimeMillis() + "_" + win.getOriginalFilename() + "_a";      
        Path pathA = Paths.get(uploadPath, fileName);

        // 파일 저장
        Files.createDirectories(pathA.getParent());
        Files.copy(and.getInputStream(), pathA, StandardCopyOption.REPLACE_EXISTING);

        

        // DB 저장
        AssetBundleEntity bundle = AssetBundleEntity.builder()
                .bundleName(bundleName)
                .bundleTitle(bundleTitle)
                .bundleDescription(bundleDescription)
                .filePath(  new String[]{pathW.toString() , pathA.toString()} )
                .uploadedAt(LocalDateTime.now())
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
    	
//    	assetBundleRepository.deleteAllById(ids);
    	
    	for (AssetBundleEntity bundle : bundles) {
            // 실제 파일 삭제
    		
    		for(String str : bundle.getFilePath() )
            deletePhysicalFile(str);

            // DB에서 삭제
            assetBundleRepository.delete(bundle);
        }    	
    }
    
    private void deletePhysicalFile(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    System.err.println("파일 삭제 실패: " + filePath);
                }
            }
        }
    }
    
    
    
    
}
