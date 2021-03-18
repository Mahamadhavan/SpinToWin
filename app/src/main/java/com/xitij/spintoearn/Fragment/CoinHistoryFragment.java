package com.xitij.spintoearn.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.xitij.spintoearn.Adapter.CoinHistoryAdapter;
import com.xitij.spintoearn.Models.CoinHistory;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.RestAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoinHistoryFragment extends Fragment {
    public Constant constant;
    public ArrayList<CoinHistory> coinHistoryArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    public CoinHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.coin_history_fragment, container, false);
        constant=new Constant(this.getActivity());
        progressBar =v.findViewById(R.id.progressbar);
        mRecyclerView=v.findViewById(R.id.recycleview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager =new LinearLayoutManager(this.getActivity());
        coinHistoryArrayList = new ArrayList<>();
        rewardPoint(constant.sharedPreferences.getString(constant.profileId,"profileId"));
        mRecyclerView.setLayoutManager(mLayoutManager);

        return v;
    }

    public void rewardPoint(String id) {

        progressBar.setVisibility(View.VISIBLE);
        coinHistoryArrayList.clear();
        String login = RestAPI.API_Coin_History + "&user_id=" + id;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(login,  new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String activity_type = object.getString("activity_type");
                        String points = object.getString("points");
                        String date = object.getString("date");
                        Log.d("Response-p", object.getString("points"));
                        coinHistoryArrayList.add(new CoinHistory(activity_type,date,points));
                    }
                    mAdapter=new CoinHistoryAdapter(coinHistoryArrayList);
                    mRecyclerView.setAdapter(mAdapter);
                    progressBar.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

}
