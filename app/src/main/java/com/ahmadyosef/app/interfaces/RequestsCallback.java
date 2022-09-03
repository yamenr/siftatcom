package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.User;

import java.util.List;

/*
Requests callback, used in AdminFragment (requests list)
Request are shown in recycler only after being retrieved successfully from firebase
*/
public interface RequestsCallback {
    void onCallback(List<ShiftRequest> requests);
}
