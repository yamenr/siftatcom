package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.ShiftRequest;

import java.util.List;

/*
callback interface, used in MainActivity
function is called only after companies data is retrieved from database, and according to user type,
user is sent to the FeedActivity (Regular employee) or AdminActivity (Company)
*/
public interface CompaniesCallback {
    void onCallback(List<Company> companies);
}
