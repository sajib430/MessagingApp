package com.example.opme200.adaptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.opme200.fragment.CallsFragment;
import com.example.opme200.fragment.ChatsFragment;
import com.example.opme200.fragment.StatusFragment;

public class FragmentsAdaptor extends FragmentPagerAdapter {


    public FragmentsAdaptor(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {

       if (position==0){
           return new ChatsFragment();
       }else if (position==1){
           return new StatusFragment();
       }else {
           return new CallsFragment();
       }


    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        if (position==0){
            return "Chats";
        }else if (position==1){
            return "Status";
        }else {
            return "Calls";
        }

    }
}
