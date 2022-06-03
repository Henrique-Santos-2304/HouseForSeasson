package com.example.multitech.houseforseasson.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.database.models.Announcement;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAnnouncement  extends RecyclerView.Adapter<AdapterAnnouncement.AnnouncementViewHolder> {
    private final List<Announcement> announcementList;
    private final Onclick onclick;

    public AdapterAnnouncement(List<Announcement> announcementList, Onclick onclick) {

        this.announcementList = announcementList;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.annoucement_item_list,parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = this.announcementList.get(position);

        Log.i("APP", "Adapter Title: "+ announcement.getTitle());
        Log.i("APP", "Adapter Description: "+ announcement.getDescription());

        String announcementTitle = announcement.getTitle();
        String announcementDescription = announcement.getDescription();
//        String announcementDate = announmcement.;
        Picasso.get().load(announcement.getUrlImg()).into(holder.img_announcement);
        holder.title.setText(announcementTitle);
        holder.description.setText(announcementDescription);
        holder.date.setText("23/04/1992 às 19:45");

        holder.itemView.setOnClickListener(view -> this.onclick.OnClickeListener(announcement));
    }

    @Override
    public int getItemCount() {
        return this.announcementList.size();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        private TextView title,description,date;
        private ImageView img_announcement;
        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.item_title_announcement);
            this.description = itemView.findViewById(R.id.item_description_announcement);
            this.date = itemView.findViewById(R.id.item_date_announcement);
            this.img_announcement = itemView.findViewById(R.id.item_img_annoucement);

        }
    }
}
