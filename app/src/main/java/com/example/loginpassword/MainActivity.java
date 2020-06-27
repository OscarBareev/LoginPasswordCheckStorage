package com.example.loginpassword;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    private final String FILE_NAME = "file.txt";
    private final String PREF_KEY = "prefKey";

    EditText loginForm;
    EditText passwordForm;
    Button loginBtn;
    Button registrationBtn;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init() {

        loginForm = findViewById(R.id.editTextLogin);
        passwordForm = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.LoginBtn);
        registrationBtn = findViewById(R.id.RegistrationBtn);
        checkBox = findViewById(R.id.checkBox);

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor checkEditor = preferences.edit();


        if (preferences.getBoolean(PREF_KEY, false)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()) {
                    checkEditor.putBoolean(PREF_KEY, true);
                } else {
                    checkEditor.putBoolean(PREF_KEY, false);
                }
                checkEditor.apply();
            }
        });


        loginBtn.setOnClickListener(myButtonClickListener);
        registrationBtn.setOnClickListener(myButtonClickListener);
    }


    View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View view) {

            String loginContent = loginForm.getText().toString().trim();
            String passwordContent = passwordForm.getText().toString().trim();

            if (loginContent.isEmpty() || passwordContent.isEmpty()) {

                Toast.makeText(MainActivity.this, R.string.addData, Toast.LENGTH_SHORT).show();

            } else {

                switch (view.getId()) {
                    case R.id.LoginBtn:

                        if (checkBox.isChecked()) {
                            readExternalFile(loginContent + passwordContent);
                        } else {
                            readFile(loginContent + passwordContent);
                        }
                        break;

                    case R.id.RegistrationBtn:

                        if (checkBox.isChecked()) {
                            writeExternalFile(loginContent + passwordContent);
                        } else {
                            writeFile(loginContent + "\n" + passwordContent);
                        }
                        break;
                }
            }
        }
    };


    private void writeFile(String content) {
        try {

            FileOutputStream outputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
            Toast.makeText(MainActivity.this,
                    R.string.regOk, Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readFile(String content) {

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(openFileInput(FILE_NAME));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line);
                Log.d("Tag", line);
                line = reader.readLine();
            }

            line = sb.toString();

            if (line.equals(content)) {
                Toast.makeText(MainActivity.this,
                        R.string.AllIsOK, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,
                        R.string.wrongData, Toast.LENGTH_SHORT).show();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    R.string.wrongData, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeExternalFile(String content) {
        File dataFile = new File(getApplicationContext().getExternalFilesDir(null), FILE_NAME);
        try (FileWriter dataWriter = new FileWriter(dataFile, true)) {
            dataWriter.append(content);
            Toast.makeText(MainActivity.this,
                    R.string.regOk, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readExternalFile(String content) {
        File dataFile = new File(getApplicationContext().getExternalFilesDir(null), FILE_NAME);

        if (dataFile.exists() && dataFile.isFile()) {

            try (FileReader dataReader = new FileReader(dataFile)) {

                Scanner scanner = new Scanner(dataReader);

                if (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.equals(content)) {
                        Toast.makeText(MainActivity.this,
                                R.string.AllIsOK, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,
                                R.string.wrongData, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity.this,
                    R.string.wrongData, Toast.LENGTH_SHORT).show();
        }
    }
}

