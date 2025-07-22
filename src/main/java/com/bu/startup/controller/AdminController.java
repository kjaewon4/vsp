package com.bu.startup.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bu.startup.dto.GameServerDTO;
import com.bu.startup.service.AdminService;
import com.bu.startup.service.GameServerService;
import com.bu.startup.service.RoomService;
import com.bu.startup.service.ServerService;
import com.bu.startup.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ServerService serverService;
    private final RoomService roomService;
    private final UserService userService;
    private final AdminService adminService;
    private final GameServerService gameServerService;
    
    @GetMapping("/uploadBundleDesc")
    public String uploadBundleDesc(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) return "login";
        return "admin/uploadBundleDesc";
    }

//    @GetMapping("/dashboard")
//    public String dashboard(Authentication authentication, Model model) {
//    	
//    	boolean isAdmin = authentication.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//        if (!isAdmin) return "login";
//        
//        model.addAttribute("rooms", roomService.getAllRooms());
//        model.addAttribute("serverStatus", serverService.getStatus());      
//        model.addAttribute("username", authentication.getName());
//        model.addAttribute("gameServers", gameServerService.getAllServers()); // NEW
//        
//        
//        return "admin/dashboard";
//    }
//
////    @PostMapping("/server/start")
////    public String startServer() {
////        serverService.start();
////        return "redirect:/admin/dashboard";
////    }
//
//    @PostMapping("/server/restart")
//    public String restartServer() {
//        serverService.restart();
//        return "redirect:/admin/dashboard";
//    }
//
//    @PostMapping("/room/update")
//    public String updateRoom(@RequestParam Long roomId, @RequestParam String roomName) {
//        roomService.updateRoomName(roomId, roomName);
//        return "redirect:/admin/dashboard";
//    }
//    
//    @PostMapping("/server/register")
//    public String registerServer(@ModelAttribute GameServerDTO serverDTO) {
//    	gameServerService.addServer(serverDTO);
//        return "redirect:/admin/dashboard";
//    }
//
//    @PostMapping("/server/run")
//    public String runServer(@RequestParam Long id) {
//    	gameServerService.runServer(id);
//        return "redirect:/admin/dashboard";
//    }
//
//    @PostMapping("/server/delete")
//    public String deleteServer(@RequestParam Long id) {
//    	gameServerService.deleteServer(id);
//        return "redirect:/admin/dashboard";
//    }
    
}