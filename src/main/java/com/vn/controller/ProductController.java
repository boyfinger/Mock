package com.vn.controller;

import com.vn.dal.CategoryDAO;
import com.vn.dal.ProductDAO;
import com.vn.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("products", productDAO.findAll());
        model.addAttribute("categories", categoryDAO.findAll());
        return "product/list";
    }

    @GetMapping("/create")
    public String createUI(Model model, RedirectAttributes redirectAttributes) {
        List<Category> categories = categoryDAO.findAll();
        if (categories.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage","No categories");
            return "redirect:/product/list";
        } else {
            model.addAttribute("categories", categories);
            return "/product/create";
        }
    }

}
