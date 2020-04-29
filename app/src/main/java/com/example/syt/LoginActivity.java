package com.example.syt;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.widget.Button;

import com.example.syt.api.model.Login;
import com.example.syt.api.model.User;
import com.example.syt.api.service.UserClient;
import com.example.syt.utils.SaveSharedPreference;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://showyourtalent.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);
    EditText username,password;
    Button submitBtn;
    RelativeLayout loginForm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.username_field);
                EditText password = findViewById(R.id.password_field);
                login(username.getText().toString(), password.getText().toString());
            }
        });

        username = findViewById(R.id.username_field);
        password = findViewById(R.id.password_field);
        submitBtn = findViewById(R.id.btn_login);
        loginForm = findViewById(R.id.loginForm);

        // Check if UserResponse is Already Logged In
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            loginForm.setVisibility(View.VISIBLE);
        }


        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Make form visible

                login(username.getText().toString(), password.getText().toString());
            }
        });

    }

    public void login(String username, String password) {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        findViewById(R.id.btn_login).setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        Login login = new Login(username, password);
        Call<User> call = userClient.login(login);

        System.out.println(username);
        System.out.println(password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    String token = response.body().getToken();
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), true, token);
                    Toast.makeText(getApplicationContext(), response.body().getToken() + " Login Sucessfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), false, "");
                    Toast.makeText(getApplicationContext(), "Invalid Creds", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG", "=======onFailure: " + t.toString());
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        findViewById(R.id.btn_login).setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        findViewById(R.id.btn_login).setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        EditText username = findViewById(R.id.username_field);
        EditText password = findViewById(R.id.password_field);
        String uname = username.getText().toString();
        String pass = password.getText().toString();

        if (uname.isEmpty()) {
            username.setError("Required!!");
            valid = false;
        } else {
            username.setError(null);
        }

        if (pass.isEmpty()) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}
