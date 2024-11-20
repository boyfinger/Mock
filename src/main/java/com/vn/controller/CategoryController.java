package com.vn.controller;

import com.vn.dal.CategoryDAO;
import com.vn.dal.ProductDAO;
import com.vn.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("products", productDAO.findAll());
        model.addAttribute("categories", categoryDAO.findAll());
        return "category/list";
    }

    @GetMapping("/create")
    public String createUI(Model model) {
        return "category/create";
    }

    @PostMapping("/create")
    public String insertCategory(
            @RequestParam("name") String name,
            RedirectAttributes redirectAttributes
    ) {
        Category category = new Category();
        category.setName(name);
        categoryDAO.save(category);

        redirectAttributes.addFlashAttribute("message","Add successfully");
        return "redirect:/category/list";
    }

    @GetMapping("/edit/{id}")
    public String editUI(Model model, @PathVariable int id) {
        Category category = categoryDAO.findById(id).orElse(null);
        if (category != null) {
            model.addAttribute("category", category);
            return "category/edit";
        } else {
            return "redirect:/category/list";
        }
    }

    @PostMapping("/edit")
    public String updateCategory(
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            RedirectAttributes redirectAttributes
    ){
        Category category = categoryDAO.findById(id).orElse(null);
        if (category != null) {
            category.setName(name);
            categoryDAO.save(category);
        }

        redirectAttributes.addFlashAttribute("message","Edit successfully");
        return "redirect:/category/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUI(Model model, @PathVariable int id, RedirectAttributes redirectAttributes) {
        categoryDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("message","Delete successfully");
        return "redirect:/category/list";
    }

}
