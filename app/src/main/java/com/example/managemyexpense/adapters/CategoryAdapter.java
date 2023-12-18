package com.example.managemyexpense.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.managemyexpense.R;
import com.example.managemyexpense.databinding.SampleCategoryItemBinding;

import com.example.managemyexpense.models.Category;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryViewHolder>{
    Context context;
    ArrayList<Category> categories;
    public interface CategoryClickLisetner{
        void onCategoryClicked(Category category);
    }
    CategoryClickLisetner categoryClickLisetner;
    public CategoryAdapter(Context context, ArrayList<Category>categories,CategoryClickLisetner categoryClickLisetner){
        this.context = context;
        this.categories = categories;
        this.categoryClickLisetner = categoryClickLisetner;
    }
    @NonNull
    @Override
    public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new categoryViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_category_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull categoryViewHolder categoryViewHolder, int i) {
        Category category=categories.get(i);
        categoryViewHolder.binding.categoryText.setText(category.getCategoryName());
        categoryViewHolder.binding.categoryIcon.setImageResource(category.getCategoryImage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            categoryViewHolder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));
        }
        categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryClickLisetner.onCategoryClicked(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class categoryViewHolder extends RecyclerView.ViewHolder {
        SampleCategoryItemBinding binding;
        public categoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleCategoryItemBinding.bind(itemView);
        }
    }
}
