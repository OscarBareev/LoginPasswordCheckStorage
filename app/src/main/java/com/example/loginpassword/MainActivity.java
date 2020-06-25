package com.example.loginpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private final String LOGIN_FILE_NAME = "login.txt";
    private final String PASSWORD_FILE_NAME = "password.txt";

    EditText loginForm;
    EditText passwordForm;
    Button loginBtn;
    Button registrationBtn;

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

        loginBtn.setOnClickListener(myButtonClickListener);
        registrationBtn.setOnClickListener(myButtonClickListener);
    }


    View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String loginContent = loginForm.getText().toString().trim();
            String passwordContent = passwordForm.getText().toString().trim();

            if (loginContent.isEmpty() || passwordContent.isEmpty()) {

                Toast.makeText(MainActivity.this, R.string.addData, Toast.LENGTH_SHORT).show();

            } else {

                switch (view.getId()) {
                    case R.id.LoginBtn:

                        if (readFile(LOGIN_FILE_NAME, loginContent)
                                && readFile(PASSWORD_FILE_NAME, passwordContent)) {
                            Toast.makeText(MainActivity.this,
                                    R.string.AllIsOK, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    R.string.wrongData, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.RegistrationBtn:

                        if (writeFile(LOGIN_FILE_NAME, loginContent)
                                && writeFile(PASSWORD_FILE_NAME, passwordContent)) {
                            Toast.makeText(MainActivity.this,
                                    R.string.regOk, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }
    };


    private boolean writeFile(String fileName, String content) {
        try {

            FileOutputStream outputStream = openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean readFile(String fileName, String content) {

        boolean isEquals = false;

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(openFileInput(fileName));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }

            line = sb.toString();


            if (line.equals(content)) {
                isEquals = true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isEquals;
    }
}

