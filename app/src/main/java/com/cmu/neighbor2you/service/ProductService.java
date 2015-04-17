package com.cmu.neighbor2you.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cmu.neighbor2you.model.Product;

import java.util.List;

/**
 * Created by xing on 4/17/15.
 */
public class ProductService extends Service implements IProductService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public boolean insertProduct(Product product) {
        return false;
    }

    @Override
    public boolean deleteProduct(Product product) {
        return false;
    }

    @Override
    public Product updateProduct(Product product) {
        return null;
    }

    @Override
    public List<Product> getProduct() {
        return null;
    }

    @Override
    public Product getProductById() {
        return null;
    }
}
