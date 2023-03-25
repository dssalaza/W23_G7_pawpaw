package com.example.petgrooming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterForRecyclerView extends RecyclerView.Adapter<CustomAdapterForRecyclerView.MyViewHolder>
{
    private Context context;
    ArrayList pet_name, pet_type, pet_breed;

    CustomAdapterForRecyclerView(Context context, ArrayList pet_name, ArrayList pet_type,
                                 ArrayList pet_breed, ArrayList pet_age)
    {
        this.context = context;
        this.pet_name = pet_name;
        this.pet_type = pet_type;
        this.pet_breed = pet_breed;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_show_pet, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.pet_name_txt.setText(String.valueOf(pet_name.get(position)));
        holder.pet_breed_txt.setText(String.valueOf(pet_breed.get(position)));
        holder.pet_type_txt.setText(String.valueOf(pet_type.get(position)));
    }

    @Override
    public int getItemCount() {
        return pet_type.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pet_name_txt, pet_type_txt, pet_breed_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pet_name_txt = itemView.findViewById(R.id.pet_name_txt);
            pet_type_txt = itemView.findViewById(R.id.pet_animaltype_txt);
            pet_breed_txt = itemView.findViewById(R.id.pet_breed_txt);


        }

    }

}
