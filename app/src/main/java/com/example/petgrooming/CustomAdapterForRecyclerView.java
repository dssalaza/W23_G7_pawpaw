package com.example.petgrooming;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
    ArrayList pet_name, pet_type, pet_breed, pet_size, pet_age, pet_condition, pet_id;
   // int position;
    CustomAdapterForRecyclerView(Context context, ArrayList pet_name, ArrayList pet_type,
                                 ArrayList pet_breed, ArrayList pet_size, ArrayList pet_age,
                                 ArrayList pet_condition, ArrayList pet_id)
    {
        this.context = context;
        this.pet_name = pet_name;
        this.pet_type = pet_type;
        this.pet_breed = pet_breed;
        this.pet_size = pet_size;
        this.pet_age = pet_age;
        this.pet_condition = pet_condition;
        this.pet_id = pet_id;


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_show_pet, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //this.position = position;
        holder.pet_name_txt.setText(String.valueOf(pet_name.get(position)));
        holder.pet_breed_txt.setText(String.valueOf(pet_breed.get(position)));
        holder.pet_type_txt.setText(String.valueOf(pet_type.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdatePetInfoActivity.class);
                intent.putExtra("pet_name_update", String.valueOf(pet_name.get(position)));
                intent.putExtra("pet_animal_type_update", String.valueOf(pet_type.get(position)));
                intent.putExtra("pet_breed_update", String.valueOf(pet_breed.get(position)));
                intent.putExtra("pet_size_update", String.valueOf(pet_size.get(position)));
                intent.putExtra("pet_age_update", String.valueOf(pet_age.get(position)));
                intent.putExtra("pet_condition_update_new", String.valueOf(pet_condition.get(position)));
                intent.putExtra("pet_id_update", String.valueOf(pet_id.get(position)));
                context.startActivity(intent);

            }
        });
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
            mainLayout = itemView.findViewById(R.id.mainRecyclerViewLayout);


        }

    }

}
