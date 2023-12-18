package com.example.managemyexpense.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.managemyexpense.R;
import com.example.managemyexpense.databinding.RowTransactionsBinding;
import com.example.managemyexpense.models.Category;
import com.example.managemyexpense.models.Transaction;
import com.example.managemyexpense.utils.Constants;
import com.example.managemyexpense.utils.Helper;
import com.example.managemyexpense.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{
    Context context;
    RealmResults<Transaction> transactions;
    public TransactionAdapter(Context context,RealmResults<Transaction>transactions){
        this.context = context;
        this.transactions = transactions;
    }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transactions,viewGroup,false));
    }


    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i) {
        Transaction transaction = transactions.get(i);
       transactionViewHolder.binding.amount.setText(String.valueOf(transaction.getAmount()));
       transactionViewHolder.binding.accLeb.setText(transaction.getAccount());
       transactionViewHolder.binding.transDate.setText(Helper.formatDate(transaction.getDate()));
       transactionViewHolder.binding.transactionCat.setText(transaction.getCategory());

       // setting up category icons which are showed in the main layout
       Category transactionCategoy=Constants.getCategoryDetails(transaction.getCategory());
       transactionViewHolder.binding.categoryIcon.setImageResource(transactionCategoy.getCategoryImage());
       transactionViewHolder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategoy.getCategoryColor()));

       if (transaction.getType().equals("Expense"))
            transactionViewHolder.binding.amount.setTextColor(context.getColor(R.color.red));
       else
            transactionViewHolder.binding.amount.setTextColor(context.getColor(R.color.green));

       //This function will help you to delete your transaction
       transactionViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               AlertDialog dialog=new AlertDialog.Builder(context).create();
               dialog.setTitle("Delete Transaction");
               dialog.setMessage("Are you sure to delete this transaction?");

               //if user press yes
               dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       ((MainActivity)context).viewModel.deleteTransaction(transaction);
                   }
               });

               //if user press no
               dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
               dialog.show();
               return false;
           }
       });
    }
    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        RowTransactionsBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionsBinding.bind(itemView);
        }
    }


}
