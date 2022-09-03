package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.ShiftRequestType;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;

import java.util.List;

/*
ShiftType callback, used for adding new shift request in TodaysFragment
 */
public interface ShiftTypeCallback {
    void onCallback(ShiftType type, ShiftRequestType srt);
}
