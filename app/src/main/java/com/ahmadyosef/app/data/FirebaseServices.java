package com.ahmadyosef.app.data;

import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.interfaces.UsersCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FirebaseServices {

    private static FirebaseServices instance;
    private FirebaseAuth auth;
    private FirebaseFirestore fire;
    private FirebaseStorage storage;
    private static Map<String, User> users;
    private Company company;
    private ArrayList<DBK> kys;
    private String dburl;
    private String apk;
    private String apid;

    private static final String TAG = "CompanySignupFragment";

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getFire() {
        return fire;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void getProjectSet()
    {

    }

    public FirebaseServices() {
        auth = FirebaseAuth.getInstance();
        fire = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        users = getUsersMap();
        company = null;
    }

    public static FirebaseServices getInstance() {
        if (instance == null)
            instance = new FirebaseServices();
        return instance;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();

        try {
            users.clear();
            getFire().collection("users_")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    users.add(document.toObject(User.class));
                                }
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return users;
    }

    public Map<String, User> getUsersMap() {
        Map<String, User> users = new HashMap<>();

        try {
            users.clear();
            fire.collection("users_")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    users.put(document.getId(), document.toObject(User.class));
                                }
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("getUsersMap(): ", e.getMessage());
        }

        return users;
    }

    public Map<String, ShiftRequest> getRequestsMap() {
        Map<String, ShiftRequest> requests = new HashMap<>();

        try {
            requests.clear();
            fire.collection("requests")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    requests.put(document.getId(), document.toObject(ShiftRequest.class));
                                }
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return requests;
    }

    public Map<String, User> getUsersMapByCompany() {
        Map<String, User> users = new HashMap<>();

        try {
            users.clear();
            fire.collection("users_")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (company.getUsers().contains(document.toObject(User.class).getUsername()))
                                        users.put(document.getId(), document.toObject(User.class));
                                }
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("getUsersMap(): ", e.getMessage());
        }

        return users;
    }

    public void removeShiftFromUser2(ShiftUser shiftUser) {
        if (users == null)
            users = getUsersMap();

        for (Map.Entry<String, User> user : users.entrySet()) {
            if (user.getValue().getUsername().equals(shiftUser.getUsername())) {
                ArrayList<Shift> removeList = new ArrayList<>();
                for (Shift shift : user.getValue().getShifts()) {
                    if (shift.getType().equals(shiftUser.getType()) &&
                            shift.getDate().equals(shiftUser.getDate())) {
                        removeList.add(shift);
                    }
                }

                user.getValue().getShifts().removeAll(removeList);

                fire.collection("users_").
                        document(user.getKey()).
                        set(user.getValue()).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("addShiftToUser: ", "User shift removed successfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("addShiftToUser: ", "Failed to remove user shift! " + e.getMessage());
                            }
                        });
                continue;
            }
        }
    }

    public void refreshUsersMap()
    {
        users = getUsersMapByCompany();
    }

    public void addShiftToUser(ShiftUser newShift) {
        for(Map.Entry<String, User> user: users.entrySet())
        {
            if (user.getValue().getUsername().equals(newShift.getUsername()))
            {
                user.getValue().getShifts().add(new Shift(UUID.randomUUID().toString(), newShift.getDate(), newShift.getType()));
                fire.collection("users_").
                        document(user.getKey()).
                        set(user.getValue()).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("addShiftToUser: ", "User shift added successfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("addShiftToUser: ", "User shift failed to be added to Firestore! " + e.getMessage());
                            }
                        });
            }
        }
    }

    public void editUserShift(ShiftUser updatedShift, ShiftUser original) {
        for(Map.Entry<String, User> user: users.entrySet())
        {
            if (user.getValue().getUsername().equals(original.getUsername()))
            {
                //user.getValue().getShifts().add(new Shift(UUID.randomUUID().toString(), newShift.getDate(), newShift.getType()));
                for(Shift shift: user.getValue().getShifts()) {
                    if (shift.getDate().equals(original.getDate()) &&
                        shift.getType().equals(original.getType()))
                    {
                        shift.setDate(updatedShift.getDate());
                        shift.setType(updatedShift.getType());
                        break;
                    }
                }
                fire.collection("users_").
                        document(user.getKey()).
                        set(user.getValue()).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("addShiftToUser: ", "User shift added successfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("addShiftToUser: ", "User shift failed to be added to Firestore! " + e.getMessage());
                            }
                        });
            }
        }
    }

    public String getNameOfUser(String shiftUser) {
        for(User user: users.values())
        {
            if (user.getUsername().equals(shiftUser))
                return user.getName();
        }
        // TODO: change it after check
        return "check!";
    }
}
