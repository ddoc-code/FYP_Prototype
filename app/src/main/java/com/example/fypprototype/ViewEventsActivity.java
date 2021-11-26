package com.example.fypprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        //call async function on page load to automatically get data from DB
        new Async().execute();
    }

    //button to refresh data from DB via asynctask
    public void getData(View view) {new Async().execute();}

    //button loads Create Event Activity
    public void createEventPage(View view) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    //asynctask to connect to DB using a different thread and retrieve data
    class Async extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            //create Connection object
            Connection conn = null;

            //grab textview
            TextView info = findViewById(R.id.connectionInfo);

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

                    //must update textviews on UI thread to avoid errors
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            info.setText("Connected to database!");
                        }
                    });

                    //read from the DB using a statement
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM ddoch001_FPprototype.events");

                    //grab linear layout
                    LinearLayout ll = findViewById(R.id.scrollingLL);
                    //remove all views from linear layout to prevent successive calls stacking repeated results (on UI thread)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ll.removeAllViews();
                        }
                    });

                    //while loop iterates through results
                    while (resultSet.next()) {

                        //save each column result
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        Date date = resultSet.getDate("date");
                        String venue = resultSet.getString("venue");
                        Float price = resultSet.getFloat("price");
                        String desc = resultSet.getString("description");
                        String owner = resultSet.getString("owner");

                        //create new textviews and set text to the above
                        TextView nameTV = new TextView(getApplicationContext());
                        nameTV.setText("Event Name: " + name);
                        TextView dateTV = new TextView(getApplicationContext());
                        dateTV.setText("Date: " + date.toString());
                        TextView venueTV = new TextView(getApplicationContext());
                        venueTV.setText("Venue: " + venue);
                        TextView priceTV = new TextView(getApplicationContext());
                        priceTV.setText("Ticket Price: £" + price.toString());
                        TextView descTV = new TextView(getApplicationContext());
                        descTV.setText("Description: " + desc);
                        TextView ownerTV = new TextView(getApplicationContext());
                        ownerTV.setText("Owner: " + owner);

                        //must update textviews on UI thread to avoid errors
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //append textviews
                                ll.addView(nameTV);
                                ll.addView(dateTV);
                                ll.addView(venueTV);
                                ll.addView(priceTV);
                                ll.addView(descTV);
                                ll.addView(ownerTV);

                                //adds padding after first result
                                if (id > 1) {
                                    nameTV.setPadding(0, 50, 0, 0);
                                }
                            }
                        });
                    }
                }

            } catch (SQLException ex) {
                System.out.println("Failed to connect to database!");
                ex.printStackTrace();

                //must update textviews on UI thread to avoid errors
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        info.setText("Failed to connect to database!");
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