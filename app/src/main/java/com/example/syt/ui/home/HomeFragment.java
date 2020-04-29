package com.example.syt.ui.home;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syt.R;
import com.example.syt.RecyclerAdapter;
import com.example.syt.api.model.Login;
import com.example.syt.api.model.User;
import com.example.syt.api.service.UserClient;
import com.example.syt.fetch.GetUsersApi;
import com.example.syt.fetch.SearchImageResponse;
import com.example.syt.fetch.SearchUsersResponse;
import com.example.syt.utils.SaveSharedPreference;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://showyourtalent.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    private static final String LOG_TAG = "heroku";
    private List<SearchImageResponse> images;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private  RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        initiateUsersApi();
        return root;
    }

    private List<SearchImageResponse>  initiateUsersApi() {
        String token = SaveSharedPreference.getTokenStatus(getActivity().getApplicationContext());
        Log.d(LOG_TAG, "Token is " + token);
        Call<SearchImageResponse[]> call = userClient.get_user_images("Token " + token);
        //progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<SearchImageResponse[]>() {

            @Override
            public void onResponse(Call<SearchImageResponse[]> call, Response<SearchImageResponse[]> response) {
                if(response.isSuccessful()) {

                    images = Arrays.asList(response.body());
                    System.out.println(images.get(0).description);
                    Log.d(LOG_TAG, "success - response is " + "Description:" + images.get(0).description);
                    recyclerAdapter = new RecyclerAdapter(getActivity().getApplicationContext(), images);
                    recyclerView.setAdapter(recyclerAdapter);


                } else {
                    //progressBar.setVisibility(View.GONE);
                    Log.d(LOG_TAG, "failure response is " + response.raw().toString());

                }
            }

            @Override
            public void onFailure(Call<SearchImageResponse[]> call, Throwable t) {
                Log.d(LOG_TAG, " Error :  " + t.getMessage());
            }

        });

        return images;
    }
}
