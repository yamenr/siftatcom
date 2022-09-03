package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.ShiftRequest;

/*
callback interface, used in RequestAdapter in new shift request approval
 first: shift is added
 second: request is deleted
*/
public interface RequestDialogueCallback {
    void onCallback(boolean b);
}