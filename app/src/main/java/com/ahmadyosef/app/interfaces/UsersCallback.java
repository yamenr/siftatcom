package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.User;
import java.util.List;

/*
Users callback, used in several fragments: Admin, Common, Todays, UsersListFragment
Used after data is retrieved from database
*/
public interface UsersCallback {
    void onCallback(List<User> usersList);
}
