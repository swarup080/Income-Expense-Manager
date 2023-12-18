package com.example.managemyexpense.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.managemyexpense.models.Transaction;
import com.example.managemyexpense.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {
   public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();
    Realm realm;
    Calendar calendar;
    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setUpDatabase();
    }
    public void getTransaction(Calendar calendar){
        this.calendar = calendar;
        //Night 12 time stamp
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
//        RealmResults<Transaction>newTransactions=realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime()).findAll();
        double income = 0;
        double expense = 0;
        double total = 0;
        RealmResults<Transaction> newTransactions = null;
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + 24 * 60 * 60 * 1000))
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .sum("amount")
                    .doubleValue();
            totalIncome.setValue(income);
            totalAmount.setValue(total);
            totalExpense.setValue(expense);
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();
            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .sum("amount")
                    .doubleValue();
        }
        totalIncome.setValue(income);
        totalAmount.setValue(total);
        totalExpense.setValue(expense);
        transactions.setValue(newTransactions);
    }
//    public void addTransaction(){
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"ahuja ahuja","Cash","Others",new Date(),500,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE,"ahuja ahuja","Cash","Rent",new Date(),600,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE,"ahuja ahuja","Cash","Rent",new Date(),700,new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE,"ahuja ahuja","Cash","Rent",new Date(),7010,new Date().getTime()));
//        realm.commitTransaction();
//    }
public void addTransaction(Transaction transaction){
    realm.beginTransaction();
    realm.copyToRealmOrUpdate(transaction);
    realm.commitTransaction();
}
public void deleteTransaction(Transaction transaction){
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransaction(calendar);
}
    void setUpDatabase(){
        realm=Realm.getDefaultInstance();
    }

}
