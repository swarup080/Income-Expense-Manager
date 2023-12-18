package com.example.managemyexpense.views.fragments;

import static com.example.managemyexpense.views.activities.MainActivity.viewModel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.managemyexpense.R;
import com.example.managemyexpense.adapters.AcccountAdapter;
import com.example.managemyexpense.adapters.CategoryAdapter;
import com.example.managemyexpense.databinding.FragmentAddTansactionBinding;
import com.example.managemyexpense.databinding.ListDialogBinding;
import com.example.managemyexpense.models.Account;
import com.example.managemyexpense.models.Category;
import com.example.managemyexpense.models.Transaction;
import com.example.managemyexpense.utils.Constants;
import com.example.managemyexpense.views.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;


public class AddTansactionFragment extends BottomSheetDialogFragment {



    public AddTansactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentAddTansactionBinding binding;
    Transaction transaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAddTansactionBinding.inflate(inflater);
        transaction=new Transaction();
        //when we click on income page
        binding.incomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.incomebtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
                binding.expensebtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                transaction.setType(Constants.INCOME);
            }
        });
        //when we click on expense page
        binding.expensebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.expensebtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
                binding.incomebtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                transaction.setType(Constants.EXPENSE);
            }
        });
        // Showing Date

        binding.date.setOnClickListener(v -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH,view.getDayOfMonth());
                    calendar.set(Calendar.MONTH,view.getMonth());
                    calendar.set(Calendar.YEAR,view.getYear());

                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMMM,YYYY");
                    String dateToshow =simpleDateFormat.format(calendar.getTime());
                    binding.date.setText(dateToshow);
                    transaction.setDate(calendar.getTime());
                    transaction.setId(calendar.getTime().getTime());
                });
                datePickerDialog.show();
            }
        });
        //category
        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogBinding listDialogBinding=ListDialogBinding.inflate(inflater);
                AlertDialog categoryDialog=new AlertDialog.Builder(getContext()).create();
                categoryDialog.setView(listDialogBinding.getRoot());


                CategoryAdapter categoryAdapter=new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickLisetner() {
                    @Override
                    public void onCategoryClicked(Category category) {
                        binding.category.setText(category.getCategoryName());
                        transaction.setCategory(category.getCategoryName());
                        categoryDialog.dismiss();
                    }
                });
                listDialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                listDialogBinding.recyclerView.setAdapter(categoryAdapter);
                categoryDialog.show();
            }
        });

        //Accounts
        binding.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogBinding listDialogBinding=ListDialogBinding.inflate(inflater);
                AlertDialog accountDialog=new AlertDialog.Builder(getContext()).create();
                accountDialog.setView(listDialogBinding.getRoot());
                ArrayList<Account>accounts=new ArrayList<>();
                accounts.add(new Account("Cash",00));
                accounts.add(new Account("PhonePay",00));
                accounts.add(new Account("GPay",00));
                accounts.add(new Account("Amazon Pay",00));
                accounts.add(new Account("Others",00));
                AcccountAdapter acccountAdapter=new AcccountAdapter(getContext(), accounts, new AcccountAdapter.accountClickListener() {
                    @Override
                    public void onAccountSelected(Account account) {
                        binding.account.setText(account.getAccountName());
                        transaction.setAccount(account.getAccountName());
                        accountDialog.dismiss();
                    }
                });

                listDialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                listDialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                listDialogBinding.recyclerView.setAdapter(acccountAdapter);
                accountDialog.show();
            }
        });
        //Save Button
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount=Double.parseDouble(binding.Amount.getText().toString());
                String note=binding.note.getText().toString();
                if (transaction.getType().equals(Constants.EXPENSE)){
                    transaction.setAmount(amount*-1);
                }else {
                    transaction.setAmount(amount);
                }
                transaction.setNote(note);
                ((MainActivity)getActivity()).viewModel.addTransaction(transaction);
                ((MainActivity)getActivity()).getTransactions();
                dismiss();
            }

//            ((MainActivity)getActivity()).MainActivity.viewModel.addTransaction(transaction);
//            ((MainActivity)getActivity()).getTransactions();
//            dismiss();
           // MainActivity mainActivity=(MainActivity) getActivity();

        });
        return binding.getRoot();
    }
}