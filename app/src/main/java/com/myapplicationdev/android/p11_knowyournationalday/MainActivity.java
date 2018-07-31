package com.myapplicationdev.android.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> alNDP;
    ArrayAdapter aaNDP;

    private int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);

        alNDP = new ArrayList<String>();
        alNDP.add("Singapore National Day is on 9 Aug");
        alNDP.add("Singapore is 53 years old");
        alNDP.add("Theme is \"We Are Singapore\"");

        aaNDP = new ArrayAdapter(this, android.R.layout.simple_list_item_1, alNDP);
        lv.setAdapter(aaNDP);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, alNDP.get(i), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //read
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean login = prefs.getBoolean("login", false);

        if (login == false) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.passphrase, null);
            final EditText etPassphrase = (EditText) passPhrase
                    .findViewById(R.id.editTextPassPhrase);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please login")
                    .setView(passPhrase)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (etPassphrase.getText().toString().equals("738964")) {
                                Snackbar.make(lv, "You logged in", Snackbar.LENGTH_LONG).show();
                                SharedPreferences spLogin = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor spLoginEdit = spLogin.edit();
                                spLoginEdit.putBoolean("login", true);
                                spLoginEdit.commit();
                            } else {
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("No Access Code", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action
        if (item.getItemId() == R.id.itemSend) {
            String[] list = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                // The action you want this intent to do;
                                // ACTION_SEND is used to indicate sending text
                                Intent email = new Intent(Intent.ACTION_SEND);
                                // Put essentials like email address, subject & body text
                                email.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{"jason_lim@rp.edu.sg"});
                                email.putExtra(Intent.EXTRA_SUBJECT,
                                        "P11KnowYourNationalDay");
                                email.putExtra(Intent.EXTRA_TEXT,
                                        "Why is email so confusing?");
                                // This MIME type indicates email
                                email.setType("message/rfc822");
                                // createChooser shows user a list of app that can handle
                                // this MIME type, which is, email
                                startActivity(Intent.createChooser(email,
                                        "Choose an Email client :"));
                                Snackbar.make(lv, "You used Email", Snackbar.LENGTH_LONG).show();
                            } else if (which == 1) {
                                String number = "12346556";  // The number on which you want to send SMS
                                Intent sms = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
                                startActivity(sms);
                                Snackbar.make(lv, "You used SMS", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.itemQuiz) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);

            final RadioGroup rgDays = (RadioGroup) quiz.findViewById(R.id.rgNationalDay);
            final RadioGroup rgAge = (RadioGroup) quiz.findViewById(R.id.rgAge);
            final RadioGroup rgTheme = (RadioGroup) quiz.findViewById(R.id.rgTheme);

            total = 0;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself!")
                    .setView(quiz)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            int selectedButtonDay = rgDays.getCheckedRadioButtonId();
                            RadioButton rbDay = (RadioButton)quiz.findViewById(selectedButtonDay);

                            if (rbDay.getText().equals("No")) {
                                total++;
                            }

                            int selectedButtonAge = rgAge.getCheckedRadioButtonId();
                            RadioButton rbAge = (RadioButton)quiz.findViewById(selectedButtonAge);

                            if (rbAge.getText().equals("Yes")) {
                                total++;
                            }

                            int selectedButtonTheme = rgTheme.getCheckedRadioButtonId();
                            RadioButton rbTheme = (RadioButton)quiz.findViewById(selectedButtonTheme);

                            if (rbTheme.getText().equals("Yes")) {
                                total++;
                            }
                            Snackbar.make(lv, total+"", Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Don't Know Lah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.itemQuit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }
}
