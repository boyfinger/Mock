package com.vn.service;

import com.vn.dal.CategoryDAO;
import com.vn.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    CategoryDAO categoryDAO;

    public Page<Category> findPaginated(int page, int size) {
        return categoryDAO.findAll(PageRequest.of(page, size));
    }

}
