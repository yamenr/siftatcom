package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.User;

import java.util.List;
import java.util.Map;

public interface AddUserToCompanyCallback {
    void onCallback(Map<String, Company> companies, User user);
}
