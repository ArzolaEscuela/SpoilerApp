package com.example.back4app.userregistrationexample;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadObjects extends AppCompatActivity {

    private String spoilerClassName() { return getResources().getString(R.string.spoiler_class_name); }
private String spoilerUserIDColumn() { return getResources().getString(R.string.spoiler_column_user_id); }
    private String spoilerSeriesNameColumn() { return getResources().getString(R.string.spoiler_column_title); }

    public ArrayList<String> dataList = new ArrayList<String>();
    public String[] entriesArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_objects);

        final FloatingActionButton logout_button = (FloatingActionButton)findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dlg = new ProgressDialog(ReadObjects.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Signing Out...");
                dlg.show();

                // logging out of Parse
                ParseUser.logOut();

                alertDisplayer("So, you're going...", "Ok...Bye-bye then");

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReadObjects.this, CreateObject.class);
                startActivity(intent);
            }
        });

        findObjects();
    }


    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReadObjects.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(ReadObjects.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void findObjects(){
        entriesArray = new String[]{};
        final ListView listView = (ListView) findViewById(R.id.listView);

        // Configure Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery(spoilerClassName());

        // Query Parameters
        query.whereEqualTo(spoilerUserIDColumn(), ParseUser.getCurrentUser());

        // Sorts the results in ascending order by the itemName field
        query.orderByAscending(spoilerSeriesNameColumn());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, final ParseException e) {
                if (e == null){
                    // Adding objects into the Array
                    for(int i= 0 ; i < objects.size(); i++){
                        String element = objects.get(i).getString(spoilerSeriesNameColumn());
                        dataList.add(element.toString());
                    }
                } else {

                }
                entriesArray = dataList.toArray(new String[dataList.size()]);

                final ArrayList<String> list  = new ArrayList<String>(Arrays.asList(entriesArray));

                ArrayAdapter<String> adapterList
                        = new ArrayAdapter<String>(ReadObjects.this, android.R.layout.simple_list_item_single_choice, entriesArray);

                listView.setAdapter(adapterList);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> adapter, View v, final int position,
                                            long id) {

                        final String value = (String) adapter.getItemAtPosition(position);

                        //Alert showing the options related with the object (Update or Delete)
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReadObjects.this)
                                .setTitle(value )
                                .setMessage("What do you want to do?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataList.remove(position);
                                        deleteObject(value);
                                        entriesArray = dataList.toArray(new String[dataList.size()]);

                                        ArrayAdapter<String> adapterList
                                                = new ArrayAdapter<String>(ReadObjects.this, android.R.layout.simple_list_item_single_choice, entriesArray);

                                        listView.setAdapter(adapterList);
                                    }
                                })
                                .setNeutralButton("Update",  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ReadObjects.this, UpdateObject.class);
                                        //Send string value to UpdateObject Activity with putExtra Method
                                        intent.putExtra("objectName", value.toString());
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog ok = builder.create();
                        ok.show();
                    }
                });
            }
        });
    }

    // Delete object
    private void deleteObject(final String value) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(spoilerClassName());

        // Query parameters based on the item name
        query.whereEqualTo(spoilerSeriesNameColumn(), value.toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> object, ParseException e) {
                if (e == null) {
                    //Delete based on the position
                    object.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        e.getMessage().toString(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            };
        });
    }
}
