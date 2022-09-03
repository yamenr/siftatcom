package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.User;

import java.util.List;
import java.util.Map;

/*
callback interface, Used in signup fragment adding new employee to company
function is called only after company data is retrieved from database and only then user is added to it
*/
public interface AddUserToCompanyCallback {
    void onCallback(Map<String, Company> companies, User user);
}
