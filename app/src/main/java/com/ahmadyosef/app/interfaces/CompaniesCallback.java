package com.ahmadyosef.app.interfaces;

import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.ShiftRequest;

import java.util.List;

public interface CompaniesCallback {
    void onCallback(List<Company> companies);
}
