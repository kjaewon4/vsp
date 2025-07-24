package com.bu.startup.controller;

import com.bu.startup.entity.AssetBundleEntity;
import com.bu.startup.entity.User;
import com.bu.startup.entity.UserProfile;
import com.bu.startup.service.AssetBundleService;
import com.bu.startup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AssetBundleService assetBundleService;

//    @GetMapping("/mypage")
//    public String mypage(Authentication authentication, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return "redirect:/login";
//        }
//
//        User currentUser = userService.getUserByUsername(authentication.getName());
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//
//        UserProfile userProfile = userService.getUserProfile(currentUser);
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
//        Page<AssetBundleEntity> uploadedBundlesPage = assetBundleService.getBundlesByAuthorAndStatuses(currentUser, Arrays.asList(ItemStatus.values()), pageable);
//        List<AssetBundleEntity> fundedBundles = assetBundleService.getFundedBundles(currentUser);
//
//        model.addAttribute("user", currentUser);
//        model.addAttribute("profile", userProfile);
//        model.addAttribute("wallet", currentUser.getWalletAddress());
//        model.addAttribute("balance", currentUser.getBalance());
//        model.addAttribute("uploadedBundlesPage", uploadedBundlesPage);
//        model.addAttribute("fundedBundles", fundedBundles);
//
//        return "mypage";
//    }

    @PostMapping("/mypage/updateProfile")
    public String updateProfile(
            @RequestParam("nickname") String nickname,
            @RequestParam("email") String email,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("walletAddress") String walletAddress,
            @RequestParam("walletBalance") double walletBalance,
            Authentication authentication) throws IOException {

        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser == null) {
            return "redirect:/login";
        }

        String profileImageUrl = userService.getUserProfile(currentUser).getProfileImageUrl();
        if (profileImage != null && !profileImage.isEmpty()) {
            // 프로필 이미지 저장 로직 (AssetBundleService의 saveFile 재활용 또는 별도 구현)
            // 여기서는 간단히 더미 경로로 대체
            profileImageUrl = "/uploads/profile/" + profileImage.getOriginalFilename(); // 실제 저장 로직 필요
        }

        userService.updateUserProfile(currentUser, nickname, email, profileImageUrl);

        return "redirect:/user/mypage";
    }
}
