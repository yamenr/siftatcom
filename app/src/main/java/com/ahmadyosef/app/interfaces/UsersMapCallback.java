package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.User;

import java.util.Map;

public interface UsersMapCallback {
    void onCallback(Map<String, User> users);
}
