package com.example.managemyexpense.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.managemyexpense.R;
import com.example.managemyexpense.databinding.RowAccountBinding;
import com.example.managemyexpense.models.Account;

import java.util.ArrayList;

public class AcccountAdapter extends RecyclerView.Adapter<AcccountAdapter.AccountViewHolder> {
    Context context;
    ArrayList<Account> accounts;
    public interface accountClickListener{
        void onAccountSelected(Account account);
    }
    accountClickListener accountClickListener;
    public AcccountAdapter(Context context,ArrayList<Account> accounts,accountClickListener accountClickListener){
        this.context = context;
        this.accounts = accounts;
        this.accountClickListener = accountClickListener;
    }
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AccountViewHolder(LayoutInflater.from(context).inflate(R.layout.row_account,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder accountViewHolder, int i) {
        Account account = accounts.get(i);
        accountViewHolder.binding.textView.setText(account.getAccountName());  //check this line
        accountViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountClickListener.onAccountSelected(account);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{

        RowAccountBinding binding;
        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowAccountBinding.bind(itemView);
        }
    }
}
