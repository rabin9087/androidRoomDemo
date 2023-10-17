package com.example.roomdemo.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.roomdemo.Product;
import com.example.roomdemo.ProductRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel{
    private final ProductRepository repository;
    private final MutableLiveData<List<Product>> searchResults;


    public MainViewModel(Application application) {
        super(application);
        repository=new ProductRepository(application);
        searchResults=repository.getSearchResults();
    }

    MutableLiveData<List<Product>> getSearchResults(){
        return searchResults;
    }

    LiveData<List<Product>> getAllProduct(){
        return getAllProduct();
    }

    public void insertProduct(Product product){
        repository.insertProduct(product);
    }
    public void findProduct(String name){
        repository.findProduct(name);
    }
    public void deleteProduct(String name){
        repository.deleteProduct(name);
    }

}