package com.example.discover.portfolio.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discover.R;
import com.example.discover.portfolio.models.User;
import com.example.discover.portfolio.views.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class PortifolioAdapter extends RecyclerView.Adapter<PortifolioAdapter.ViewHolder> {

    //Data member variables to hold user information and application context
    Context context;
    List<Integer> ids = new ArrayList<>();
    List<String> avatars = new ArrayList<>();
    List<String> usernames = new ArrayList<>();
    List<String> jotTitles = new ArrayList<>();
    List<String> bios = new ArrayList<>();
    List<String> educations = new ArrayList<>();
    List<String> skills = new ArrayList<>();
    List<String> emails = new ArrayList<>();
    List<String> phones = new ArrayList<>();
    List<String> webUrls = new ArrayList<>();
    List<String> facebookUrls = new ArrayList<>();
    List<String> linkedInUrls = new ArrayList<>();
    List<String> latitudes = new ArrayList<>();
    List<String> longitudes = new ArrayList<>();

    public PortifolioAdapter(Context context, List<User> users) {
        this.context = context;
        for (User user:users) {
            ids.add(user.getId());
            avatars.add(user.getAvatar());
            usernames.add(user.getUsername());
            jotTitles.add(user.getJobTitle());
            bios.add(user.getBio());
            educations.add(user.getEducation());
            skills.add(user.getSkills());
            emails.add(user.getEmail());
            phones.add(user.getPhone());
            webUrls.add(user.getWebUrl());
            facebookUrls.add(user.getFacebookUrl());
            linkedInUrls.add(user.getLinkedInUrl());
            latitudes.add(user.getLatitude());
            longitudes.add(user.getLongitude());
        }
    }

    //Default constructor

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View itemView;
        public final ImageView avatar;
        public final TextView username;
        public final TextView bio;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.itemView = itemView;
            avatar = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.user);
            bio = itemView.findViewById(R.id.user_bio);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    //Pass portfolio information to the Profile Activity using intent
                    intent.putExtra("Id", ids.get(getAdapterPosition()));
                    intent.putExtra("Avatar", avatars.get(getAdapterPosition()));
                    intent.putExtra("Username", usernames.get(getAdapterPosition()));
                    intent.putExtra("JobTitle", jotTitles.get(getAdapterPosition()));
                    intent.putExtra("Bio", bios.get(getAdapterPosition()));
                    intent.putExtra("Education", educations.get(getAdapterPosition()));
                    intent.putExtra("Skills", skills.get(getAdapterPosition()));
                    intent.putExtra("Email", emails.get(getAdapterPosition()));
                    intent.putExtra("Phone", phones.get(getAdapterPosition()));
                    intent.putExtra("Web", webUrls.get(getAdapterPosition()));
                    intent.putExtra("Facebook", facebookUrls.get(getAdapterPosition()));
                    intent.putExtra("LinkedIn", linkedInUrls.get(getAdapterPosition()));
                    intent.putExtra("Latitude", latitudes.get(getAdapterPosition()));
                    intent.putExtra("Longitude", longitudes.get(getAdapterPosition()));

                    context.startActivity(intent);

                }
            });
        }
    }

    public PortifolioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user_data, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortifolioAdapter.ViewHolder holder, int position) {
        holder.avatar.setImageURI(Uri.parse(avatars.get(holder.getAdapterPosition())));
        holder.username.setText(usernames.get(holder.getAdapterPosition()));
        holder.bio.setText(bios.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }
}