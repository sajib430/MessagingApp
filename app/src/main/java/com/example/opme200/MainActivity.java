package com.example.opme200;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.opme200.activity.GroupChatsActivity;
import com.example.opme200.activity.SettingsActivity;
import com.example.opme200.activity.SignInActivity;
import com.example.opme200.adaptor.FragmentsAdaptor;
import com.example.opme200.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        FragmentsAdaptor adaptor = new FragmentsAdaptor(getSupportFragmentManager());
        binding.viewPager.setAdapter(adaptor);
        binding.tabLayoutMain.setupWithViewPager(binding.viewPager);


//        binding.viewPager.setAdapter(new FragmentsAdaptor(getSupportFragmentManager()));
//        binding.tabLayoutMain.setupWithViewPager(binding.viewPager);
//


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                signOut();
                break;
            case R.id.groupChats:
                Intent intent = new Intent(MainActivity.this, GroupChatsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void signOut() {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}