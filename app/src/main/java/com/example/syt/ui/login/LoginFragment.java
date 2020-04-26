package com.example.syt.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.syt.R;
import com.example.syt.api.model.Login;
import com.example.syt.api.model.User;
import com.example.syt.api.service.UserClient;
import com.example.syt.utils.SaveSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://showyourtalent.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_login, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        loginViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        root.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) root.findViewById(R.id.username_field);
                EditText password = (EditText) root.findViewById(R.id.password_field);
                login(username.getText().toString(), password.getText().toString());
            }
        });
        return root;
    }

    private static String token;
    public void login(String uname, String pass){
        Login login = new Login(uname, pass);
        Call<User> call = userClient.login(login);

        System.out.println(uname);
        System.out.println(pass);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
               if(response.isSuccessful()){
                   String token = response.body().getToken();
                   SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(), true, token);
                   Toast.makeText(getActivity(), response.body().getToken() + " Login Sucessfull", Toast.LENGTH_SHORT).show();


               }
               else{
                   Toast.makeText(getActivity(), "Invalid Creds", Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG", "=======onFailure: " + t.toString());
                t.printStackTrace();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
