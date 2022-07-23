package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;

import java.util.List;
import java.util.Map;

public interface FirebaseCallback {
    void onCallback(boolean b);
    void onCallback(ShiftType type);
    void onCallback(Map<String, User> users);
    void onCallback(List<User> usersList);
}
