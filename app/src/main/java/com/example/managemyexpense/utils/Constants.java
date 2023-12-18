package com.example.managemyexpense.utils;

import com.example.managemyexpense.R;
import com.example.managemyexpense.models.Category;

import java.util.ArrayList;

public class Constants {
    public static String INCOME = "Income";
    public static String EXPENSE = "Expense";
    public static int DAILY = 0;
    public static int MONTHLY = 1;
    public static int CALENDAR = 2;
    public static int SUMMARY = 3;
    public static int NOTES = 4;
    public static int SELECTED_TAB = 0;

    public static ArrayList<Category> categories;
    public static void setCategories(){
        categories = new ArrayList<>();
        categories.add(new Category(R.drawable.dollar_note_icon,"Salary",R.color.green));
        categories.add(new Category(R.drawable.investment,"Investment",R.color.HalfRed));
        categories.add(new Category(R.drawable.loan,"Loan",R.color.green));
        categories.add(new Category(R.drawable.rent,"Rent",R.color.HalfRed));
        categories.add(new Category(R.drawable.business,"Business",R.color.HalfRed));
        categories.add(new Category(R.drawable.others,"Others",R.color.HalfRed));
    }
    public static Category getCategoryDetails(String categoryName){
        for (Category cat: categories) {
           if (cat.getCategoryName().equals(categoryName))
               return cat;
        }
        return null;
    }
}
