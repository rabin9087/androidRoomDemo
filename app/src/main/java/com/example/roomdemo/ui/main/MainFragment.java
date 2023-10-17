package com.example.roomdemo.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.net.sip.SipSession;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roomdemo.Product;
import com.example.roomdemo.ProductListAdapter;
import com.example.roomdemo.R;

import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private ProductListAdapter adapter;

    private TextView productId;
    private EditText productName;
    private EditText productQuantity;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        productId=getView().findViewById(R.id.productID);
        productName=getView().findViewById(R.id.productName);
        productQuantity=getView().findViewById(R.id.productQuantity);

        listenerSetup();
        observerSetup();
        recyclerSetup();

        // TODO: Use the ViewModel
    }

    private void clearFields(){
        productQuantity.setText("");
        productId.setText("");
        productName.setText("");
    }

    private void listenerSetup(){
        Button addButton= getView().findViewById(R.id.addButton);
        Button findButton= getView().findViewById(R.id.findButton);
        Button deleteButton= getView().findViewById(R.id.deleteButton);

        addButton.setOnClickListener(view -> {
            String name=productName.getText().toString();
            String quantity=productQuantity.getText().toString();

            if(!name.equals("") && !quantity.equals("")){
                Product product=new Product(name,Integer.parseInt(quantity));
                mViewModel.insertProduct(product);
                clearFields();
            } else {
                productId.setText("Incomplete information");
            }
        });

        findButton.setOnClickListener(view -> mViewModel.findProduct(productName.getText().toString()));

        deleteButton.setOnClickListener(view -> {
            mViewModel.deleteProduct(productName.getText().toString());
            clearFields();
        });

    }

    private void observerSetup(){
        mViewModel.getAllProduct().observe(getViewLifecycleOwner(), products -> adapter.setProductList(products));

        mViewModel.getSearchResults().observe(getViewLifecycleOwner(), products -> {
            if (products.size()>0){
                productId.setText(String.format(Locale.getDefault(),"%d", products.get(0).getId()));
                productName.setText(products.get(0).getName());
                productQuantity.setText(String.format(Locale.getDefault(),"%d", products.get(0).getQuantity()));
            } else {
                productId.setText("No Match");
            }
        });
    }

    private void recyclerSetup(){
        RecyclerView recyclerView;
        adapter=new ProductListAdapter(R.layout.product_list_item);
        recyclerView=getView().findViewById(R.id.product_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

}