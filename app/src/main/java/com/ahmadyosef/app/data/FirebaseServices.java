package com.ahmadyosef.app.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

public class FirebaseServices {

    private static FirebaseServices instance;
    private FirebaseAuth auth;
    private FirebaseFirestore fire;
    private FirebaseStorage storage;

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getFire() {
        return fire;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public FirebaseServices()
    {
        auth = FirebaseAuth.getInstance();
        fire = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static FirebaseServices getInstance()
    {
        if (instance == null)
            instance = new FirebaseServices();

        return instance;
    }

    public ArrayList<Shift> shiftToday(Date date, String username)
    {
        ArrayList<Shift> shifts = new ArrayList<>();
        // TODO: retrieve today's shifts according to username

        return shifts;
    }
}
