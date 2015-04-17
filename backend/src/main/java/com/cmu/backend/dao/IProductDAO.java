package com.cmu.backend.dao;

import com.cmu.backend.model.Product;
import com.cmu.backend.model.User;

import java.util.List;

/**
 * Created by xing on 4/16/15.
 */
public interface IProductDAO {

    public boolean insertProduct(Product product);
    public boolean deleteProduct(Product product);
    public Product updateProduct(Product product);
    public List<Product> getProduct();
    public Product getProductById();
}
