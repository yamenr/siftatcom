package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.User;

import java.util.List;

public interface UsersCallback {
    void onCallback(List<User> usersList);
}
