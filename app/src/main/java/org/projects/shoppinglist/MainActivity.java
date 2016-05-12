package org.projects.shoppinglist;


import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import com.firebase.client.Firebase;

import com.firebase.ui.FirebaseListAdapter;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {





//creating the adapter

    ArrayAdapter<Product> adapter;
    ListView listView;
    ArrayList<Product> bag = new ArrayList<Product>();



    Firebase zref;
    FirebaseListAdapter<Product> fireAdapter;
    public FirebaseListAdapter getMyAdapter() {return fireAdapter;}
    //public Product getItem (int index) {return getMyAdapter().getItem(index);}




    Product lastDeletedProduct;
    int lastDeletedPosition;
    public void saveCopy()
    {
        lastDeletedPosition = listView.getCheckedItemPosition();
        lastDeletedProduct = fireAdapter.getItem(lastDeletedPosition);
    }

//clear command part

    public void showDialog() {
        //showing our dialog.
        ClearDialog dialog = new ClearDialog() {
            @Override
            protected void positiveClick() {
                //Here we override the methods and can now
                //do something
                Toast toast = Toast.makeText(getApplicationContext(),
                        "List is cleared", Toast.LENGTH_LONG);
                toast.show();
                //bag.clear();
                zref.setValue(null);

                getMyAdapter().notifyDataSetChanged();
            }

            @Override
            protected void negativeClick() {
                //Here we override the method and can now do something
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No changes in the list", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        //Here we show the dialog
        //The tag "MyFragement" is not important for us.
        dialog.show(getFragmentManager(), "MyFragment");
    }

//Here's the part when we create data for the list.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //we save the whole array, in bag, in case we need it

        if(savedInstanceState!=null){

            bag = savedInstanceState.getParcelableArrayList("bag"); }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        zref = new Firebase("https://sweltering-torch-5979.firebaseio.com//items");
        fireAdapter = new FirebaseListAdapter<Product>(this, Product
                .class, android.R.layout.simple_list_item_checked, zref){
            @Override
            protected void populateView(View v, Product product, int i){
                TextView text = (TextView) v.findViewById(android.R.id.text1);
                text.setText(product.toString());
            }
        };






        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview

       // adapter =  new ArrayAdapter<Product>(this,
         //       android.R.layout.simple_list_item_checked,bag );

        //setting the adapter on the listview
        listView.setAdapter(fireAdapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText foodlist = (EditText) findViewById(R.id.foodin);
                String foodtext = foodlist.getText().toString();

                EditText foodnum = (EditText) findViewById(R.id.foodnumber);
                String foodquantity = foodnum.getText().toString();

                Spinner foodspinner = (Spinner) findViewById(R.id.spinnerfood);
                String foodpackagetype = String.valueOf(foodspinner.getSelectedItem());


               // bag.add(new Product(foodquantity, foodpackagetype, foodtext));

                Product p = new Product(foodquantity, foodpackagetype, foodtext);
                zref.push().setValue(p);


                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();



            }

        });

        /*
        final Button clearButton = (Button) findViewById(R.id.ClearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(clearButton);

            }

        });
*/
        Button deleteButton = (Button) findViewById(R.id.DeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                saveCopy();

            for(int i = fireAdapter.getCount() - 1; i>= 0; i--)
            {
                if(checkedItems.get(i))
                {
                    //bag.remove(fireAdapter.getItem(i));
                    getMyAdapter().getRef(i).setValue(null);
                }
            }
                getMyAdapter().notifyDataSetChanged();


                final View parent = listView;
                Snackbar snackbar = Snackbar
                        .make(parent, "Item Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               // bag.add(lastDeletedPosition,lastDeletedProduct);

                                zref.push().setValue(lastDeletedProduct);

                                getMyAdapter().notifyDataSetChanged();
                                Snackbar snackbar = Snackbar.make(parent, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                snackbar.show();


            }
        });

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1) //exited our preference screen
        {
            Toast toast =
                    Toast.makeText(getApplicationContext(), "Settings updated!", Toast.LENGTH_LONG);
            toast.setText("Settings updated!");
            toast.show();
            //here you could put code to do something.......
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



//ballala
    public void setPreferences() {
        //Here we create a new activity and we instruct the
        //Android system to start it
        Intent intent = new Intent(this, SettingsActivity.class);
        //startActivity(intent); //this we can use if we DONT CARE ABOUT RESULT

        //we can use this, if we need to know when the user exists our preference screens
        startActivityForResult(intent, 1);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_all:

                showDialog();

        //Code to run when the Clear is clicked
                return true;
            case R.id.action_settings:


                setPreferences();
        //Code to run when the settings item is clicked
                return true;
            case R.id.action_share:

                convertListToString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); //MIME type

                intent.putExtra(Intent.EXTRA_TEXT, convertListToString()); //add the text to t
                startActivity(intent);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

            @Override
            public void onSaveInstanceState(Bundle savedInstanceState){
            savedInstanceState.putParcelableArrayList("bag", bag);
        }


    public String convertListToString()
    {
        String result = "Here is the shopping list: ";
        for (int i = 0; i<fireAdapter.getCount();i++)
        {
            Product p = (Product) fireAdapter.getItem(i);
            result = result + "\n"+ p;
        }
        return result;
    }


    }




