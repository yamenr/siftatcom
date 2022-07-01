package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.User;

import java.util.List;

public interface RequestsCallback {
    void onCallback(List<ShiftRequest> requests);
}
