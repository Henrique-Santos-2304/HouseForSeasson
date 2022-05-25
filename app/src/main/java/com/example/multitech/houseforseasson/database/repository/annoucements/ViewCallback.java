package com.example.multitech.houseforseasson.database.repository.annoucements;

import com.example.multitech.houseforseasson.database.models.Announcement;

import java.util.List;

public interface ViewCallback {
    void showData(List<Announcement> announcementList);
}
