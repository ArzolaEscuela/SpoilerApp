package com.example.back4app.userregistrationexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;

public class UpdateObject extends AppCompatActivity
{

    private String spoilerClassName() { return getResources().getString(R.string.spoiler_class_name); }
    private String spoilerUserIDColumn() { return getResources().getString(R.string.spoiler_column_user_id); }
    private String spoilerSeriesNameColumn() { return getResources().getString(R.string.spoiler_column_title); }
    private String spoilerContentsColumn() { return getResources().getString(R.string.spoiler_column_spoiler); }

    private EditText spoilerSeriesName;
    private EditText spoilerContents;
    private String getObjectId;

    private Button create_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_object);
        create_button = (Button) findViewById(R.id.btnCreate);
        spoilerSeriesName = (EditText) findViewById(R.id.edtSpoilerSeriesName);
        spoilerContents = (EditText) findViewById(R.id.edtSpoilerContents);

        final Intent element = getIntent();

        // Recover string from other activity
        final String objectName = element.getStringExtra("objectName").toString();

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveObject();
            }
        });

        // Recover the CurrentUser Id with the getObjectId method
        String currentUser = ParseUser.getCurrentUser().getObjectId();
        //
        final ParseObject obj = ParseObject.createWithoutData("_User",currentUser);

        //Configure Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery(spoilerClassName());

        // Query Parameters
        query.whereEqualTo(spoilerUserIDColumn(), obj);
        query.whereEqualTo(spoilerSeriesNameColumn(), objectName);

        // How we need retrive exactly one result we can use the getFirstInBackground method
        query.getFirstInBackground(
                new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    // Insert the retrieve Content inside the Input at the view
                    spoilerSeriesName.setText(objectName);
                    spoilerContents.setText(object.getString(spoilerContentsColumn()));
                    getObjectId = object.getObjectId().toString();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    public void saveObject(){
        final Editable spoilerSeriesNameUpdate = spoilerSeriesName.getText();
        final Editable spoilerContentsUpdate = spoilerContents.getText();


        ParseQuery<ParseObject> query = ParseQuery.getQuery(spoilerClassName());

        // Retrieve the object by id
        query.getInBackground(getObjectId, new GetCallback<ParseObject>() {
            public void done(ParseObject spoilerList, ParseException e) {
                if (e == null) {
                    spoilerList.put(spoilerSeriesNameColumn(), spoilerSeriesNameUpdate.toString());
                    spoilerList.put(spoilerContentsColumn(), spoilerContentsUpdate.toString());

                    spoilerList.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(UpdateObject.this, ReadObjects.class);
                                startActivity(intent);
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
            }
        });
    }
}