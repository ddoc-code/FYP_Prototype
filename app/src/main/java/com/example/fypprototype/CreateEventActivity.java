package com.example.fypprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    //button to write user input to DB via asynctask
    public void writeData(View view) {new Async().execute();}

    //asynctask to connect to DB using a different thread
    class Async extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            //create Connection object
            Connection conn = null;

            try {
                //connection details
                //url format is "jdbc:mysql://hostname:port/databasename"
                String url1 = "jdbc:mysql://igor.gold.ac.uk:3307/ddoch001_FPprototype";

                //login details
                String user = "ddoch001";
                String password = "temppass2";

                //initiate connection here
                conn = DriverManager.getConnection(url1, user, password);
                if (conn != null) {
                    System.out.println("Connected to database!");
                    System.out.println();

                    //get user inputs
                    EditText nameET = findViewById(R.id.nameInput);
                    String nameInput = nameET.getText().toString();

                    EditText dateET = findViewById(R.id.dateInput);
                    String dateInput = dateET.getText().toString();

                    EditText venueET = findViewById(R.id.venueInput);
                    String venueInput = venueET.getText().toString();

                    EditText priceET = findViewById(R.id.priceInput);
                    String priceInput = priceET.getText().toString();

                    EditText descET = findViewById(R.id.descInput);
                    String descInput = descET.getText().toString();

                    //write to the DB using a PreparedStatement
                    PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO ddoch001_FPprototype.events (name, date, venue, price, description, owner) VALUES ('" +
                            nameInput + "', '" + dateInput + "', '" + venueInput + "', " + priceInput + ", '" + descInput + "', 'admin')");
                    System.out.println(preparedStatement);
                    preparedStatement.executeUpdate();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView info = findViewById(R.id.errorMessage);
                            info.setText("Your event was created successfully!");
                        }
                    });

                }
            } catch (SQLException ex) { //catch error when user inputs info in wrong format
                System.out.println("Failed to write to DB!");
                ex.printStackTrace();

                //here
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView info = findViewById(R.id.errorMessage);
                        info.setText("Your event could not be added to the database! Please check all fields are formatted correctly.");
                    }
                });
            }

            //close connection in finally block
            finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return null;
        }
    }
}