package com.example.onlinephonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinephonebook.model.Contacts;
import com.example.onlinephonebook.remote.ApiClient;
import com.example.onlinephonebook.remote.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnSignUp;
    private ProgressDialog loading;
    EditText etxtCell, etxtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignUp = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        etxtCell = findViewById(R.id.etxt_cell);
        etxtPassword = findViewById(R.id.etxt_password);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cell = etxtCell.getText().toString();
                String password = etxtPassword.getText().toString();

                //validation


                if (cell.length() != 11) {
                    etxtCell.setError("Invalid cell!");
                    etxtCell.requestFocus();

                } else if (password.isEmpty()) {
                    etxtPassword.setError("Password can not be empty! ");
                    etxtPassword.requestFocus();

                } else {
                    //call login method
                    login(cell, password);
                }

            }
        });

    }



        private void login(String cell,String password) {

            loading=new ProgressDialog(this);
            loading.setMessage("Please wait....");
            loading.show();

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

            Call<Contacts> call = apiInterface.login(cell, password);
            call.enqueue(new Callback<Contacts>() {
                @Override
                public void onResponse(Call<Contacts> call, Response<Contacts> response) {

                    String value = response.body().getValue();
                    String message = response.body().getMassage();

                    if (value.equals("success"))
                    {
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }



                    else {
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Contacts> call, Throwable t) {

                    loading.dismiss();
                    Toast.makeText(LoginActivity.this, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }





