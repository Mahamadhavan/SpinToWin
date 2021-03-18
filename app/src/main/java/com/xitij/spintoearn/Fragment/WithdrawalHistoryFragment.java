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

import com.xitij.spintoearn.Adapter.WithdrawalHistoryAdapter;
import com.xitij.spintoearn.Models.WithdrawHistory;
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
public class WithdrawalHistoryFragment extends Fragment {
    public Constant constant;
    public ArrayList<WithdrawHistory> withdrawHistoryArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private WithdrawHistory withdrawHistory;
    public WithdrawalHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.withd_history_fragment, container, false);
        constant=new Constant(this.getActivity());

        mRecyclerView=v.findViewById(R.id.recycleview_widthdrow);
        progressBar =v.findViewById(R.id.progressbar);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager =new LinearLayoutManager(this.getActivity());
        withdrawHistoryArrayList = new ArrayList<>();
        WithdrawHistorylog(constant.sharedPreferences.getString(constant.profileId,"profileId"));
       // withdrawHistoryArrayList.add(new WithdrawHistory("sam","5-5-12","3","Pending") );
       /* mAdapter=new WithdrawalHistoryAdapter(withdrawHistoryArrayList);
        mRecyclerView.setAdapter(mAdapter);*/
        mRecyclerView.setLayoutManager(mLayoutManager);

        return v;
    }

    public void WithdrawHistorylog(String id) {
        progressBar.setVisibility(View.VISIBLE);
        withdrawHistoryArrayList.clear();
        String url = RestAPI.API_Coin_withdrawal_History + "&user_id=" + id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url,  new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response-c", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray1 = jsonObject.getJSONArray(Constant.AppSid);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        String status = object.getString("status") ;
                        String activity_type = object.getString("activity_type");
                        String points = object.getString("points");
                        String date = object.getString("date");
                        withdrawHistoryArrayList.add(new WithdrawHistory(activity_type,date,points,status));
                    }
                    mAdapter = new WithdrawalHistoryAdapter(withdrawHistoryArrayList);
                    mRecyclerView.setAdapter(mAdapter);
                    progressBar.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Log.d("Response-arry", "No data in array "+e.getMessage());
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }


}
