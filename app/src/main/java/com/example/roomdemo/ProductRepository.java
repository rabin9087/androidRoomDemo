package com.example.roomdemo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ProductRepository {

    private final MutableLiveData<List<Product>> searchResults= new MutableLiveData<>();

    private void asyncFinished(List<Product> results){
        searchResults.setValue(results);

    }

    private static class QueryAsyncTask extends AsyncTask<String, Void, List<Product>>{

        private final ProductDao asyncTaskDao;
        private ProductRepository delegate= null;

        QueryAsyncTask(ProductDao dao){
            asyncTaskDao=dao;
        }

        @Override
        protected List<Product> doInBackground(final String... strings) {
            return asyncTaskDao.findProduct(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            delegate.asyncFinished(products);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Product, Void, Void>{
        private final ProductDao asyncTaskDao;

        InsertAsyncTask(ProductDao dao){
            asyncTaskDao =dao;
        }

        @Override
        protected Void doInBackground(final Product... products) {
            asyncTaskDao.insertProduct(products[0]);
            return null;
        }

    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void>{

        private final ProductDao asyncTaskDao;
        DeleteAsyncTask(ProductDao dao){
            asyncTaskDao=dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            asyncTaskDao.deleteProduct(strings[0]);
            return null;
        }
    }

    private final LiveData<List<Product>> allProducts;
    private final ProductDao productDao;
    public ProductRepository(Application application){
        ProductRoomDatabase db;
        db=ProductRoomDatabase.getDatabase(application);
        productDao=db.productDao();
        allProducts=productDao.getAllProducts();
    }

    public void insertProduct(Product newProduct){
        InsertAsyncTask task =new InsertAsyncTask(productDao);
        task.execute(newProduct);
    }

    public void deleteProduct(String name){
        DeleteAsyncTask task= new DeleteAsyncTask(productDao);
        task.execute(name);
    }

    public void findProduct(String name){
        QueryAsyncTask task =new QueryAsyncTask(productDao);
        task.delegate=this;
        task.execute(name);
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public MutableLiveData<List<Product>> getSearchResults(){
        return searchResults;
    }
}
