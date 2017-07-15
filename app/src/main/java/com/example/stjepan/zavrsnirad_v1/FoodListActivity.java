package com.example.stjepan.zavrsnirad_v1;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stjepan.zavrsnirad_v1.data.Food;
import com.example.stjepan.zavrsnirad_v1.data.FoodAdapter;
import com.example.stjepan.zavrsnirad_v1.data.FoodDbHelper;

import java.util.List;


public class FoodListActivity extends AppCompatActivity/* implements LoaderManager.LoaderCallbacks<Cursor>*/ {
    private static final int FOOD_LOADER = 0;

    private final static String TAG= FoodListActivity.class.getName().toString();
    private FoodDbHelper mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist_recycler);

        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);

        RecyclerView productView = (RecyclerView)findViewById(R.id.recycler_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productView.setLayoutManager(linearLayoutManager);
        productView.setHasFixedSize(true);

        mDatabase = new FoodDbHelper(this);
        List<Food> allFood = mDatabase.listFood();

        if(allFood.size() > 0){
            productView.setVisibility(View.VISIBLE);
            FoodAdapter mAdapter = new FoodAdapter(this, allFood);
            productView.setAdapter(mAdapter);

        }else {
            productView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no product in the database. Start adding now", Toast.LENGTH_LONG).show();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(FoodListActivity.this, EditorActivity.class);
                //startActivity(intent);
                addTaskDialog();
            }
        });


       //ListView foodListView = (ListView) findViewById(R.id.list);
       // View emptyView = findViewById(R.id.empty_view);
       // foodListView.setEmptyView(emptyView);

        //Setup an adapter to create a list item for each row of food data in the cursor
       /* foodCursorAdapter = new FoodCursorAdapter(this, null);
        foodListView.setAdapter(foodCursorAdapter);

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(FoodListActivity.this, EditorActivity.class);

                Uri currentFoodUri = ContentUris.withAppendedId(FoodEntry.CONTENT_URI, id);
                intent.setData(currentFoodUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(FOOD_LOADER, null, this);
*/
    }

    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.activity_editor_recycler, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText fatField = (EditText)subView.findViewById(R.id.enter_fat);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new product");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD PRODUCT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final double fat = Double.parseDouble(fatField.getText().toString());

                if(TextUtils.isEmpty(name) || fat <= 0){
                    Toast.makeText(FoodListActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    Food newFood = new Food(name, fat);
                    mDatabase.addFood(newFood);

                    //refresh the activity
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FoodListActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }

/*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = {
                FoodEntry._ID,
                FoodEntry.COLUMN_FOOD_NAME,
                FoodEntry.COLUMN_FAT_TOTAL
        };
        return new CursorLoader(this,
                FoodEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        foodCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        foodCursorAdapter.swapCursor(null);
    }*/
}
















