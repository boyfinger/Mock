package com.vn.controller;

import com.vn.dal.CategoryDAO;
import com.vn.dal.ProductDAO;
import com.vn.model.Category;
import com.vn.model.Product;
import com.vn.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final String defaultImage = "https://via.placeholder.com/50";
    private static final int pageSize = 6;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    FileService fileService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<Product> productPage = productDAO.findAll(PageRequest.of(page, pageSize));
        model.addAttribute("productPage", productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

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

    @PostMapping("/create")
    public String insertProduct(
            @RequestParam("name") String name,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("price") double price,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes
    ) {
        Product product = new Product();
        Category category = categoryDAO.findById(categoryId).orElse(null);
        if (category != null){
            product.setCategory(category);
            product.setName(name);
            product.setPrice(price);

            if (image == null || image.isEmpty()){
                product.setImage(defaultImage);
            } else {
                try{
                    String imageURL = fileService.upload(image);
                    product.setImage(imageURL);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            productDAO.save(product);
        }

        redirectAttributes.addFlashAttribute("message","Product created");
        return "redirect:/product/list";
    }

    @GetMapping("/edit/{id}")
    public String editUI(Model model, @PathVariable int id) {
        Product product = productDAO.findById(id).orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryDAO.findAll());
        return "/product/edit";
    }

    @PostMapping("/edit")
    public String editProduct(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("price") double price,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes
    ){
        Product product = productDAO.findById(id).orElse(null);
        if (product != null){
            product.setName(name);
            product.setCategory(categoryDAO.findById(categoryId).orElse(null));
            product.setPrice(price);

            if (image != null && !image.isEmpty()){
                try{
                    String imageURL = fileService.upload(image);
                    product.setImage(imageURL);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            productDAO.save(product);
        }

        redirectAttributes.addFlashAttribute("message","Product updated");
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUI(Model model, @PathVariable int id, RedirectAttributes redirectAttributes) {
        productDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("message","Product deleted");
        return "redirect:/product/list";
    }

}
