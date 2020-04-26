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

import com.example.syt.R;
import com.example.syt.fetch.GetUsersApi;
import com.example.syt.fetch.SearchUsersResponse;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final String API_BASE_URL = "http://showyourtalent.herokuapp.com";
    private static final String LOG_TAG = "heroku";
    private List<SearchUsersResponse> users;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        List<SearchUsersResponse> usersList = initiateUsersApi("", root);

        return root;
    }

    private List<SearchUsersResponse>  initiateUsersApi(String query, final View view) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetUsersApi api = retrofit.create(GetUsersApi.class);
        Call<SearchUsersResponse[]> call = api.getUsersList("json", query);
        //progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<SearchUsersResponse[]>() {

            @Override
            public void onResponse(Call<SearchUsersResponse[]> call, Response<SearchUsersResponse[]> response) {
                if(response.isSuccessful()) {

                    users = Arrays.asList(response.body());
                    System.out.println(users.get(0).username);
                    Log.d(LOG_TAG, "success - response is " + "Username:" + users.get(0).username);
                    TableLayout tl;
                    tl = (TableLayout) view.findViewById(R.id.fragment_home_table);
                    TableRow tr = new TableRow(getActivity());
                    tr.setId(0);
                    tr.setBackgroundResource(R.color.colorAccent);
                    tr.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    TextView tv1 = new TextView(getActivity());
                    tv1.setText("Username");
                    tv1.setTextColor(Color.BLACK);
                    tv1.setTextSize(20);
                    tv1.setBackgroundResource(R.drawable.border);
                    tv1.setPadding(10, 10, 10, 10);
                    tr.addView(tv1);

                    TextView tv2 = new TextView(getActivity());
                    tv2.setText("Full Name");
                    tv2.setTextColor(Color.BLACK);
                    tv2.setTextSize(20);
                    tv2.setBackgroundResource(R.drawable.border);
                    tv2.setPadding(10, 10, 10, 10);
                    tr.addView(tv2);

                    TextView tv3 = new TextView(getActivity());
                    tv3.setText("Email");
                    tv3.setTextColor(Color.BLACK);
                    tv3.setTextSize(20);
                    tv3.setBackgroundResource(R.drawable.border);
                    tv3.setPadding(10, 10, 10, 10);
                    tr.addView(tv3);

                    tl.addView(tr, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    for (SearchUsersResponse user : users) {

                        TableRow tr2 = new TableRow(getActivity());
                        tr2.setBackgroundResource(R.color.design_default_color_error);
                        tr2.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        //TEXTVIEWS********
                        TextView tv4 = new TextView(getActivity());
                        tv4.setText(user.username);
                        tv4.setTextColor(Color.BLACK);
                        tv4.setTextSize(20);
                        tv4.setBackgroundResource(R.drawable.border);
                        tv4.setPadding(10, 10, 10, 10);
                        tr2.addView(tv4);

                        TextView tv5 = new TextView(getActivity());
                        tv5.setText(user.first_name + " "+ user.last_name);
                        tv5.setTextColor(Color.BLACK);
                        tv5.setTextSize(20);
                        tv5.setBackgroundResource(R.drawable.border);
                        tv5.setPadding(10, 10, 10, 10);
                        tr2.addView(tv5);

                        TextView tv6 = new TextView(getActivity());
                        tv6.setText(user.email);
                        tv6.setTextColor(Color.BLACK);
                        tv6.setTextSize(20);
                        tv6.setBackgroundResource(R.drawable.border);
                        tv6.setPadding(10, 10, 10, 10);
                        tr2.addView(tv6);

                        tl.addView(tr2, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    }
                    //progressBar.setVisibility(View.GONE);

                } else {
                    //progressBar.setVisibility(View.GONE);
                    Log.d(LOG_TAG, "failure response is " + response.raw().toString());

                }
            }

            @Override
            public void onFailure(Call<SearchUsersResponse[]> call, Throwable t) {
                Log.d(LOG_TAG, " Error :  " + t.getMessage());
            }

        });

        return users;
    }
}
