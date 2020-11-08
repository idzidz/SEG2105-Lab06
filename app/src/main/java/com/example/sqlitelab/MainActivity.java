package com.example.sqlitelab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView dbContent;
    TextView idView;
    EditText productBox;
    EditText skuBox;

    ArrayList<String> listItem;
    ArrayAdapter adapter;
    MyDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idView = (TextView) findViewById(R.id.productID);
        productBox = (EditText) findViewById(R.id.productName);
        skuBox = (EditText) findViewById(R.id.productSku);

        dbContent = findViewById(R.id.dbList);

        listItem = new ArrayList<>();
        dbHandler = new MyDBHandler(this);
        viewData();

//        dbContent.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
//                String query = "SELECT " +
//                //Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void viewData() {
        Cursor cursor = dbHandler.viewData();
        while (cursor.moveToNext()){
            listItem.add(" Name: " + cursor.getString(1) + "\n Unit: " + cursor.getString(2));
            //listItem.add("ID: " + cursor.getString(0) + " Name: " + cursor.getString(1) + " Unit: " + cursor.getString(2));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        dbContent.setAdapter(adapter);
    }


    public void newProduct (View view) {

        String userProduct = productBox.getText().toString();
        String userSKU = skuBox.getText().toString();

        int sku = Integer.parseInt(userSKU);
        Product product = new Product(userProduct, sku);

        MyDBHandler dbHandler = new MyDBHandler(this);
        dbHandler.addProduct(product);

        productBox.setText("");
        skuBox.setText("");

        //Whenever a product is added, clear the listItem and reload data
        listItem.clear();
        viewData();

        Context context = getApplicationContext();
        String text = (userProduct + " has been added to unit " + userSKU);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void lookupProduct (View view) {

        MyDBHandler dbHandler = new MyDBHandler(this);
        Product product = dbHandler.findProduct(productBox.getText().toString());

        if (product != null) {
            idView.setText(String.valueOf(product.getID()));
            skuBox.setText(String.valueOf(product.getSku()));
        } else {
            idView.setText("No Match Found");
        }
    }


    public void removeProduct (View view) {

        MyDBHandler dbHandler = new MyDBHandler(this);
        boolean result = dbHandler.deleteProduct(productBox.getText().toString());
        //boolean result = false;

        if (result) {
            Context context = getApplicationContext();
            String text = (productBox.getText() + " has been deleted");
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            idView.setText("Record Deleted");
            productBox.setText("");
            skuBox.setText("");
            //Whenever a product is deleted, clear the listItem and reload data
            listItem.clear();
            viewData();
        }
        else
            idView.setText("No Match Found");
    }

    public void clearButton (View view){
        productBox.setText("");
        skuBox.setText("");
        idView.setText("All text cleared");
        Context context = getApplicationContext();
        String text = ("All fields have been cleared");
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}