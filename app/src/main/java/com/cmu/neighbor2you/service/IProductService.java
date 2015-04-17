package com.cmu.neighbor2you.service;

import com.cmu.neighbor2you.model.Product;

import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public interface IProductService {
    public boolean insertProduct(Product product);
    public boolean deleteProduct(Product product);
    public Product updateProduct(Product product);
    public List<Product> getProduct();
    public Product getProductById();
}
