package com.example.managemyexpense.views.activities;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import androidx.lifecycle.ViewModelProviders;

import com.example.managemyexpense.adapters.TransactionAdapter;
import com.example.managemyexpense.models.Transaction;
import com.example.managemyexpense.utils.Constants;
import com.example.managemyexpense.utils.Helper;
import com.example.managemyexpense.viewmodels.MainViewModel;
import com.example.managemyexpense.views.fragments.AccountFragment;
import com.example.managemyexpense.views.fragments.AddTansactionFragment;
import com.example.managemyexpense.R;
import com.example.managemyexpense.databinding.ActivityMainBinding;
import com.example.managemyexpense.views.fragments.MoreFragment;
import com.example.managemyexpense.views.fragments.StatsFragment;
import com.example.managemyexpense.views.fragments.TransactionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
//Here Binding used to access views directly
    ActivityMainBinding binding;
    Calendar calendar;

    public static MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.setCategories();
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).
                get(MainViewModel.class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transactions");
        calendar = Calendar.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionFragment());
        transaction.commit();
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (menuItem.getItemId() == R.id.trans){
                    transaction.replace(R.id.content, new TransactionFragment());
                    //getSupportFragmentManager().popBackStack();
                } else if (menuItem.getItemId() == R.id.stats) {
                    transaction.replace(R.id.content, new StatsFragment());
                    //transaction.addToBackStack(null);
                } else if (menuItem.getItemId() == R.id.account) {
                    transaction.replace(R.id.content, new AccountFragment());
                   // transaction.addToBackStack(null);
                } else if (menuItem.getItemId() == R.id.more) {
                    transaction.replace(R.id.content, new MoreFragment());
                   // transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });
    }

    //set up menu for top tool bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void getTransactions(){
        viewModel.getTransaction(calendar);
    }
}