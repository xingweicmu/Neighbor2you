package com.cmu.neighbor2you.util;

import android.util.Log;

import com.cmu.neighbor2you.model.Product;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mangobin on 15-4-10.
 */
public class WalmartUtil {
    public static final String WALMART_API =" http://api.walmartlabs.com/v1/items?apiKey=kartxhufycgb4fhqj4jbtq78&upc=";

    public Product getItemInfo(String barcode) {
        String response = sendReqToWalmart(barcode);
        Log.v("responsesss",response);
        return parseJSONResponseAsProduct(response);
    }

    private Product parseJSONResponseAsProduct(String response) {
        if(!response.contains("items")) {
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        if(jsonObject.has("errors")) {
            return null;
        }
        JsonObject item = jsonObject.get("items").getAsJsonArray().get(0).getAsJsonObject();
        Product product = new Product();
        product.setName(item.get("name").getAsString());
        if(item.has("salePrice")){
            product.setPrice(item.get("salePrice").getAsDouble());
        }
        if(item.has("longDescription")) {
            product.setDescription(item.get("longDescription").getAsString());
        }
        if(item.has("thumbnailImage")) {
            product.setThumbnailImage(item.get("thumbnailImage").getAsString());
        }
        return product;
    }

    private String sendReqToWalmart(String barcode) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(WALMART_API+barcode);
            Log.v("urrrl",url.toString());
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
