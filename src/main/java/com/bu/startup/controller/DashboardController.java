package com.bu.startup.controller;

import com.bu.startup.ServerInstance;
import com.bu.startup.VirtualStartUpApplication;
import com.bu.startup.entity.AssetBundleEntity;
import com.bu.startup.entity.User;
import com.bu.startup.service.*;
import com.bu.startup.type.CategoryType;
import com.bu.startup.type.ItemStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final AssetBundleService assetBundleService;
    private final PostService postService;
    private final ServerService serverService;
    private final LikeService likeService;

    @GetMapping
    public String showDashboard(Authentication authentication, Model model,
                                @RequestParam(name = "fragment", required = false) String fragmentName) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("profile", currentUser);
        System.out.println("DashboardController.showDashboard currentUser: " + currentUser);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            model.addAttribute("sidebarFragment", "sidebar_admin");
            model.addAttribute("sidebarTemplate", "fragments/sidebar_admin");
            model.addAttribute("initialFragmentUrl","/dashboard/admin/info");

        } else {
            model.addAttribute("sidebarFragment", "sidebar_user");
            model.addAttribute("sidebarTemplate", "fragments/sidebar_user");
            model.addAttribute("initialFragmentUrl","/dashboard/user/profile");
        }

        // 기본적으로 사용자 프로필을 보여주도록 설정
        return "dashboard";
    }

    // --- User Dashboard Fragments ---

    @GetMapping("/user/profile")
    public String getUserProfileFragment(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("profile", currentUser);
        return "fragments/dashboard/user_profile_fragment";
    }

    @GetMapping("/user/uploadedBundles")
    public String getUserUploadedBundlesFragment(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("profile", currentUser);
        List<AssetBundleEntity> uploadedBundles = assetBundleService.getBundlesByAuthor(currentUser);
        model.addAttribute("uploadedBundles", uploadedBundles);
        model.addAttribute("categories", CategoryType.values()); // bundleList.html 형식에 필요
        model.addAttribute("statuses", ItemStatus.values()); // bundleList.html 형식에 필요
        return "fragments/dashboard/user_uploaded_bundles_fragment";
    }

    @GetMapping("/user/fundedBundles")
    public String getUserFundedBundlesFragment(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("profile", currentUser);
        List<AssetBundleEntity> fundedBundles = assetBundleService.getFundedBundles(currentUser);
        model.addAttribute("fundedBundles", fundedBundles);
        model.addAttribute("categories", CategoryType.values());
        model.addAttribute("statuses", ItemStatus.values());
        return "fragments/dashboard/user_funded_bundles_fragment";
    }

    @GetMapping("/user/myPosts")
    public String getUserMyPostsFragment(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("profile", currentUser);
        model.addAttribute("myPosts", postService.getPostsByAuthor(currentUser));
        return "fragments/dashboard/user_my_posts_fragment";
    }

    @GetMapping("/user/myComments")
    public String getUserMyCommentsFragment(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("profile", currentUser);
        model.addAttribute("myComments", postService.getCommentsByAuthor(currentUser));
        return "fragments/dashboard/user_my_comments_fragment";
    }

    @GetMapping("/user/likedBundles")
    public String getUserLikedBundlesFragment(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("profile", currentUser);
        model.addAttribute("likedBundles", likeService.getLikedBundlesByUser(currentUser));
        model.addAttribute("categories", CategoryType.values());
        model.addAttribute("statuses", ItemStatus.values());
        return "fragments/dashboard/user_liked_bundles_fragment";
    }

    // --- Admin Dashboard Fragments ---

    @GetMapping("/admin/info")
    public String getAdminInfoFragment(Authentication authentication, Model model) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("admin", currentUser);
        model.addAttribute("profile", userService.getUserProfile(currentUser));
        return "fragments/dashboard/admin_info_fragment";
    }

    @GetMapping("/admin/pendingBundles")
    public String getPendingBundlesFragment(Model model) {
        model.addAttribute("pendingBundles", assetBundleService.getPendingBundles());
        return "admin/bundleApproval";
    }

    @GetMapping("/admin/registerBundle")
    public String getRegisterBundleFragment(Model model) {
        model.addAttribute("categories", CategoryType.values());
        return "bundleUpload";
    }

    @GetMapping("/admin/allBundles")
    public String getAllBundlesFragment(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        model.addAttribute("bundlePage", assetBundleService.getAllBundles(pageable));
        model.addAttribute("categories", CategoryType.values());
        model.addAttribute("statuses", ItemStatus.values());
        return "bundleList";
    }

    @GetMapping("/admin/serverManagement")
    public ModelAndView getServerManagementFragment(Authentication authentication, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        // 이전에 AssetBundleController에 있던 로직을 그대로 가져옵니다.
        if(authentication == null) return new ModelAndView("login");

        String userId = authentication.getName();

        ModelAndView mav = new ModelAndView("admin/assetBundle");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        mav.addObject("assetBundles", assetBundleService.getAllBundles(pageable));

        String appid = VirtualStartUpApplication.PhotonAppID.get(0);
        String args = String.format(
                "-room=%s -mode=%s -scenename=%s -username=%s",
                "Samsung", "admin", "Room", userId
        );

        mav.addObject("params", args);

        List<AssetBundleController.AppInfo> appInfoList = IntStream.range(0, VirtualStartUpApplication.PhotonAppID.size())
                .mapToObj(index -> new AssetBundleController.AppInfo(String.valueOf(index), VirtualStartUpApplication.PhotonAppID.get(index)))
                .collect(Collectors.toList());

        mav.addObject("appidList", appInfoList );

        serverService.cleanUpDeadProcesses();
        Collection<ServerInstance> runningServers = serverService.getConnectedServers();

        System.out.println(runningServers);

        mav.addObject("serversJson", runningServers);

        return mav;
    }
}