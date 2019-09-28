package com.arunsinghsaab98.project_feedback.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arunsinghsaab98.project_feedback.R;
import com.squareup.picasso.Picasso;

public class RecyclerHolder extends RecyclerView.ViewHolder {

    public TextView userName;
    public ImageView imageView;

    public RecyclerHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.cvText);
        imageView = itemView.findViewById(R.id.CVimage);
    }

//    public void setUserName(String user_name)
//    {
//         userName = itemView.findViewById(R.id.cvText);
//    }
//    public void setImage (Context context, String image)
//    {
//        imageView = itemView.findViewById(R.id.CVimage);
//        Picasso.with(context).load(image).into(imageView);
//    }
}
