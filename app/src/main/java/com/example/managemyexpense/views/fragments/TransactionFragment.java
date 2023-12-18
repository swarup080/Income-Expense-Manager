package com.example.managemyexpense.views.fragments;

import static com.example.managemyexpense.views.activities.MainActivity.viewModel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.managemyexpense.R;
import com.example.managemyexpense.adapters.TransactionAdapter;
import com.example.managemyexpense.databinding.FragmentTransactionBinding;
import com.example.managemyexpense.models.Transaction;
import com.example.managemyexpense.utils.Constants;
import com.example.managemyexpense.utils.Helper;
import com.example.managemyexpense.viewmodels.MainViewModel;
import com.example.managemyexpense.views.activities.MainActivity;

import java.util.Calendar;

import io.realm.RealmResults;


public class TransactionFragment extends Fragment {

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentTransactionBinding binding;
    Calendar calendar;

   public MainViewModel viewModel ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater);
        /*
        Because of require activity () this fragment can use MainViewModel and update data live ,, no need to refresh the page
         */
        viewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).
                get(MainViewModel.class);

        //viewModel=new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        calendar = Calendar.getInstance();
        updateDate();
        //pervious date
        binding.prevdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.SELECTED_TAB == Constants.DAILY) {
                    calendar.add(Calendar.DATE, -1);
                }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                    calendar.add(Calendar.MONTH, -1);
                }
                updateDate();
            }
        });
        //next date
        binding.nextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.SELECTED_TAB == Constants.DAILY) {
                    calendar.add(Calendar.DATE, 1);
                }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                    calendar.add(Calendar.MONTH, 1);
                }
                updateDate();
            }
        });
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddTansactionFragment().show(requireFragmentManager(),null);
            }
        });


        /* Before the integration of database i used arraylist of transaction to add the data
          ArrayList<Transaction> transactions=new ArrayList<>();
        transactions.add(new Transaction("income","ahuja ahuja","Cash","Others",new Date(),500,1));
        transactions.add(new Transaction(Constants.EXPENSE,"ahuja ahuja","Cash","rent",new Date(),700,4));
        transactions.add(new Transaction(Constants.INCOME,"ahuja ahuja","Cash","Others",new Date(),5500,2));
        transactions.add(new Transaction(Constants.EXPENSE,"ahuja ahuja","Cash","Others",new Date(),5100,1));

        viewModel.addTransaction();
        */


        binding.transactionList.setLayoutManager(new LinearLayoutManager(getContext()));
        //Observe all the transactions
        viewModel.transactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(@Nullable RealmResults<Transaction> transactions) {
                TransactionAdapter transactionAdapter = new TransactionAdapter(getActivity(),transactions);
                binding.transactionList.setAdapter(transactionAdapter);
                if (transactions.size()>0){
                    binding.empty.setVisibility(View.GONE);
                }else{
                    binding.empty.setVisibility(View.VISIBLE);
                }
            }
        });
        //Observing total amount
        viewModel.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double aDouble) {
                binding.totalShow.setText(String.valueOf(aDouble));
            }
        });
        //Observing total expense
        viewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double aDouble) {
                binding.expenseshow.setText(String.valueOf(aDouble));
            }
        });
        //Observing total income
        viewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double aDouble) {
                binding.incomeshow.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransaction(calendar);

        //function for tab layout
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                }
                else if (tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return binding.getRoot();
    }
    void updateDate(){
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            binding.date.setText(Helper.formatDate(calendar.getTime()));
        }else if (Constants.SELECTED_TAB == Constants.MONTHLY){
            binding.date.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransaction(calendar);
    }
}