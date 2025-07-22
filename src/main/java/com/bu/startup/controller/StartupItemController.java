package com.bu.startup.controller;

import com.bu.startup.entity.StartupItem;
import com.bu.startup.repo.StartupItemRepository;
import com.bu.startup.service.StartupItemService;
import com.bu.startup.type.CategoryType;
import com.bu.startup.type.ItemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class StartupItemController {

    private final StartupItemService startupItemService;

    @GetMapping("/items")
    public String items(@RequestParam(required = false) CategoryType category,
                        @RequestParam(required = false) ItemStatus status,
                        Model model) {
        List<StartupItem> items;

        if (category != null && status != null) {
            items = startupItemService.getItemsByCategoryAndStatus(category, status);
        } else if (category != null) {
            items = startupItemService.getItemsByCategory(category);
        } else if (status != null) {
            items = startupItemService.getItemsByStatus(status);
        } else {
            items = startupItemService.getAllItems();
        }
        model.addAttribute("items", items);
        model.addAttribute("categories", CategoryType.values());
        model.addAttribute("statuses", ItemStatus.values());
        return "items";
    }
}
