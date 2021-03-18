package com.xitij.spintoearn.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xitij.spintoearn.Fragment.CoinHistoryFragment;
import com.xitij.spintoearn.Fragment.WithdrawalHistoryFragment;
import com.xitij.spintoearn.R;

public class TabAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public TabAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new CoinHistoryFragment();
        }
        else
        {
            return new WithdrawalHistoryFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.coin_history);
            case 1:
                return mContext.getString(R.string.withdrawl_history);
            default:
                return null;
        }
    }
}
