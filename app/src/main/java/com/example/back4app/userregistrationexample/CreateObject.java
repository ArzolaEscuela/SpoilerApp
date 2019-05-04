package com.example.back4app.userregistrationexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateObject extends AppCompatActivity
{
    private String spoilerClassName() { return getResources().getString(R.string.spoiler_class_name); }
private String spoilerUserIDColumn() { return getResources().getString(R.string.spoiler_column_user_id); }
    private String spoilerSeriesNameColumn() { return getResources().getString(R.string.spoiler_column_title); }
    private String spoilerContentsColumn() { return getResources().getString(R.string.spoiler_column_spoiler); }

    private EditText spoilerSeriesName;
    private EditText spoilerContents;
    private Button create_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_object);

        spoilerSeriesName = (EditText) findViewById(R.id.edtSpoilerSeriesName);
        spoilerContents = (EditText) findViewById(R.id.edtSpoilerContents);
        create_button = findViewById(R.id.btnCreate);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Please, ");
                if (isEmptyText(spoilerSeriesName)) {
                    validationError = true;
                    validationErrorMessage.append("insert a series title for the spoilers ");
                }

                validationErrorMessage.append(".");
                if (validationError) {
                    Toast.makeText(CreateObject.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    saveObject();
                }
            }
        });
    }

    private boolean isEmptyText(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public void saveObject(){
        // Configure Query
        ParseObject spoilerList = new ParseObject(spoilerClassName());

        // Store an object
        spoilerList.put(spoilerSeriesNameColumn(), spoilerSeriesName.getText().toString());
        spoilerList.put(spoilerContentsColumn(), spoilerContents.getText().toString());
        spoilerList.put(spoilerUserIDColumn(), ParseUser.getCurrentUser());

        // Saving object
        spoilerList.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(CreateObject.this, ReadObjects.class);
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
    }
}
