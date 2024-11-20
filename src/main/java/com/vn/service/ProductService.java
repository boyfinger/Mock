package com.vn.service;

import com.vn.dal.ProductDAO;
import com.vn.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public Page<Product> findPaginated(int page, int size) {
        return productDAO.findAll(PageRequest.of(page, size));
    }

}
