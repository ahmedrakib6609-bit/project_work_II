package com.example.bloodbankbd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PharmaActivity extends AppCompatActivity {

    private RecyclerView pharmaciesRecyclerView;
    private RecyclerView medicinesRecyclerView;
    private PharmacyAdapter pharmacyAdapter;
    private MedicineAdapter medicineAdapter;
    private List<Pharmacy> pharmacyList;
    private List<Medicine> medicineList;
    private List<Medicine> originalMedicineList;
    private AutoCompleteTextView etSearchMedicine;
    private ChipGroup chipGroupCompany;
    private TabLayout tabLayout;
    private LinearLayout pharmaciesContainer;
    private LinearLayout medicinesContainer;
    private String currentCompanyFilter = "All";
    private ArrayAdapter<String> suggestionAdapter;
    private List<String> allMedicineNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma);

        // Back button
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Page title
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Pharmacy & Medicine");

        // Initialize TabLayout
        tabLayout = findViewById(R.id.tabLayout);
        pharmaciesContainer = findViewById(R.id.pharmaciesContainer);
        medicinesContainer = findViewById(R.id.medicinesContainer);

        // Setup tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    pharmaciesContainer.setVisibility(View.VISIBLE);
                    medicinesContainer.setVisibility(View.GONE);
                } else {
                    pharmaciesContainer.setVisibility(View.GONE);
                    medicinesContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Setup Pharmacies RecyclerView
        pharmaciesRecyclerView = findViewById(R.id.pharmaciesRecyclerView);
        pharmacyList = new ArrayList<>();
        loadSamplePharmacies();
        pharmacyAdapter = new PharmacyAdapter(pharmacyList);
        pharmaciesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pharmaciesRecyclerView.setAdapter(pharmacyAdapter);

        // Setup Medicines RecyclerView
        medicinesRecyclerView = findViewById(R.id.medicinesRecyclerView);
        medicineList = new ArrayList<>();
        originalMedicineList = new ArrayList<>();
        loadSampleMedicines();

        medicineAdapter = new MedicineAdapter(medicineList, medicine -> {
            // This is an extra callback, main toast is already in Adapter
            // You can remove this if you don't need it
        });
        medicinesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        medicinesRecyclerView.setAdapter(medicineAdapter);

        // Setup Search with AutoCompleteTextView
        etSearchMedicine = findViewById(R.id.etSearchMedicine);
        setupSearchWithSuggestions();

        // Setup Company Filter Chips
        chipGroupCompany = findViewById(R.id.chipGroupCompany);
        setupCompanyFilters();

        // FAB for selling medicine
        FloatingActionButton fabSellMedicine = findViewById(R.id.fabSellMedicine);
        fabSellMedicine.setOnClickListener(v -> {
            Toast.makeText(this, "Sell Medicine - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void setupSearchWithSuggestions() {
        // Collect all medicine names for suggestions
        allMedicineNames = new ArrayList<>();
        Set<String> uniqueNames = new HashSet<>();
        for (Medicine medicine : originalMedicineList) {
            if (!uniqueNames.contains(medicine.getName())) {
                uniqueNames.add(medicine.getName());
                allMedicineNames.add(medicine.getName());
            }
        }

        // Stylish Custom Adapter with 85% opacity background
        suggestionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                allMedicineNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(ContextCompat.getColor(PharmaActivity.this, android.R.color.black));
                textView.setTextSize(14);
                textView.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                textView.setPadding(24, 16, 24, 16);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(ContextCompat.getColor(PharmaActivity.this, android.R.color.black));
                textView.setTextSize(14);
                textView.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                textView.setPadding(24, 16, 24, 16);

                android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
                drawable.setColor(0xD8FFFFFF);
                drawable.setCornerRadius(16f);
                drawable.setStroke(1, 0xFFE0E0E0);
                textView.setBackground(drawable);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_medicine, 0, 0, 0);
                textView.setCompoundDrawablePadding(16);

                if (position < getCount() - 1) {
                    textView.setPadding(24, 16, 24, 8);
                }

                return view;
            }
        };

        etSearchMedicine.setAdapter(suggestionAdapter);
        etSearchMedicine.setThreshold(1);

        try {
            android.graphics.drawable.ColorDrawable softBackground = new android.graphics.drawable.ColorDrawable(0xD8FFFFFF);
            etSearchMedicine.setDropDownBackgroundDrawable(softBackground);
        } catch (Exception e) {
            try {
                etSearchMedicine.setDropDownBackgroundResource(android.R.color.white);
            } catch (Exception ex) {}
        }

        etSearchMedicine.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        etSearchMedicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                List<String> filteredSuggestions = new ArrayList<>();
                for (String name : allMedicineNames) {
                    if (name.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                        filteredSuggestions.add(name);
                    }
                }

                suggestionAdapter = new ArrayAdapter<String>(PharmaActivity.this,
                        android.R.layout.simple_dropdown_item_1line, filteredSuggestions) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view;
                        textView.setTextColor(ContextCompat.getColor(PharmaActivity.this, android.R.color.black));
                        textView.setTextSize(14);
                        textView.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                        textView.setPadding(24, 16, 24, 16);
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView textView = (TextView) view;
                        textView.setTextColor(ContextCompat.getColor(PharmaActivity.this, android.R.color.black));
                        textView.setTextSize(14);
                        textView.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                        textView.setPadding(24, 16, 24, 16);

                        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
                        drawable.setColor(0xD8FFFFFF);
                        drawable.setCornerRadius(16f);
                        drawable.setStroke(1, 0xFFE0E0E0);
                        textView.setBackground(drawable);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_medicine, 0, 0, 0);
                        textView.setCompoundDrawablePadding(16);

                        return view;
                    }
                };
                etSearchMedicine.setAdapter(suggestionAdapter);

                if (filteredSuggestions.size() > 0) {
                    etSearchMedicine.showDropDown();
                } else {
                    etSearchMedicine.dismissDropDown();
                }

                applyFilters(query);
                medicineAdapter.setSearchQuery(query);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etSearchMedicine.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            etSearchMedicine.setText(selected);
            applyFilters(selected);
            medicineAdapter.setSearchQuery(selected);

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearchMedicine.getWindowToken(), 0);
        });

        etSearchMedicine.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearchMedicine.getWindowToken(), 0);

                String query = etSearchMedicine.getText().toString();
                applyFilters(query);
                medicineAdapter.setSearchQuery(query);

                return true;
            }
            return false;
        });
    }

    private void setupCompanyFilters() {
        chipGroupCompany.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentCompanyFilter = "All";
                Chip allChip = findViewById(R.id.chipAll);
                if (allChip != null) {
                    allChip.setChecked(true);
                }
            } else {
                View selectedChip = group.findViewById(checkedIds.get(0));
                if (selectedChip instanceof Chip) {
                    currentCompanyFilter = ((Chip) selectedChip).getText().toString();
                }
            }
            String currentQuery = etSearchMedicine.getText().toString();
            applyFilters(currentQuery);
        });
    }

    private void applyFilters(String searchQuery) {
        List<Medicine> filteredList = new ArrayList<>(originalMedicineList);

        if (!searchQuery.isEmpty()) {
            List<Medicine> searchFiltered = new ArrayList<>();
            for (Medicine medicine : filteredList) {
                if (medicine.getName().toLowerCase(Locale.getDefault()).contains(searchQuery.toLowerCase(Locale.getDefault()))) {
                    searchFiltered.add(medicine);
                }
            }
            filteredList = searchFiltered;
        }

        if (!currentCompanyFilter.equals("All")) {
            List<Medicine> companyFiltered = new ArrayList<>();
            for (Medicine medicine : filteredList) {
                if (medicine.getCompany().toLowerCase(Locale.getDefault()).contains(currentCompanyFilter.toLowerCase(Locale.getDefault()))) {
                    companyFiltered.add(medicine);
                }
            }
            filteredList = companyFiltered;
        }

        medicineAdapter.updateList(filteredList);
    }

    private void loadSamplePharmacies() {
        pharmacyList.add(new Pharmacy(
                "Apollo Pharmacy",
                "Dhaka, Banani",
                "Open 24/7",
                "4.5",
                "01712345678",
                "https://maps.google.com/?q=23.7925,90.4042"
        ));

        pharmacyList.add(new Pharmacy(
                "Square Pharmaceuticals Ltd.",
                "Dhaka, Mohakhali",
                "8:00 AM - 10:00 PM",
                "4.8",
                "01876543210",
                "https://maps.google.com/?q=23.7775,90.4050"
        ));

        pharmacyList.add(new Pharmacy(
                "Incepta Pharmacy",
                "Dhaka, Dhanmondi",
                "9:00 AM - 9:00 PM",
                "4.3",
                "01911223344",
                "https://maps.google.com/?q=23.7450,90.3750"
        ));

        pharmacyList.add(new Pharmacy(
                "Beximco Pharma",
                "Dhaka, Gulshan",
                "10:00 AM - 8:00 PM",
                "4.6",
                "01698765432",
                "https://maps.google.com/?q=23.7900,90.4100"
        ));

        pharmacyList.add(new Pharmacy(
                "Oshudhi Pharmacy",
                "Chittagong, Agrabad",
                "9:00 AM - 11:00 PM",
                "4.2",
                "01512345678",
                "https://maps.google.com/?q=22.3350,91.8300"
        ));
    }

    private void loadSampleMedicines() {
        originalMedicineList.add(new Medicine(
                "Napa Extra",
                "Square Pharmaceuticals",
                "Paracetamol 500mg",
                "15",
                "In Stock",
                R.drawable.napa_extra
        ));

        originalMedicineList.add(new Medicine(
                "Napa 500mg",
                "Beximco Pharma",
                "Paracetamol",
                "25",
                "In Stock",
                R.drawable.napa_500mg
        ));

        originalMedicineList.add(new Medicine(
                "Seclo 20mg",
                "Square Pharmaceuticals",
                "Omeprazole",
                "12",
                "In Stock",
                R.drawable.seclo_20mg
        ));

        originalMedicineList.add(new Medicine(
                "Fexo 120mg",
                "Incepta Pharmaceuticals",
                "Fexofenadine",
                "8",
                "Limited Stock",
                R.drawable.fexo_120mg
        ));

        originalMedicineList.add(new Medicine(
                "Ace 2.5mg",
                "Incepta Pharmaceuticals",
                "Ramipril",
                "10",
                "In Stock",
                R.drawable.ace_25mg
        ));

        originalMedicineList.add(new Medicine(
                "Cef-3 200mg",
                "Beximco Pharma",
                "Cefixime",
                "6",
                "In Stock",
                R.drawable.cef_3_200mg
        ));

        originalMedicineList.add(new Medicine(
                "Maxpro 20mg",
                "Healthcare Pharmaceuticals",
                "Omeprazole",
                "14",
                "In Stock",
                R.drawable.maxpro_20mg
        ));

        originalMedicineList.add(new Medicine(
                "Monas 10mg",
                "Healthcare Pharmaceuticals",
                "Montelukast",
                "9",
                "Limited Stock",
                R.drawable.monas_10mg
        ));

        originalMedicineList.add(new Medicine(
                "Rivotril 0.5mg",
                "Roche Bangladesh",
                "Clonazepam",
                "5",
                "Prescription Required",
                R.drawable.rivotril_05mg
        ));

        originalMedicineList.add(new Medicine(
                "Insulin Mixtard",
                "Novo Nordisk",
                "Human Insulin",
                "3",
                "Prescription Required",
                R.drawable.insulin_mixtard
        ));

        medicineList.addAll(originalMedicineList);
    }

    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navDonors = findViewById(R.id.nav_donors);
        LinearLayout navUrgent = findViewById(R.id.nav_urgent);
        LinearLayout navPharma = findViewById(R.id.nav_pharma);
        LinearLayout navChat = findViewById(R.id.nav_chat);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(PharmaActivity.this, HomeActivity.class));
            finish();
        });

        navDonors.setOnClickListener(v -> {
            startActivity(new Intent(PharmaActivity.this, DonorsActivity.class));
            finish();
        });

        navUrgent.setOnClickListener(v -> {
            startActivity(new Intent(PharmaActivity.this, UrgentActivity.class));
            finish();
        });

        navPharma.setOnClickListener(v -> {
            Toast.makeText(this, "Already on Pharma page", Toast.LENGTH_SHORT).show();
        });

        navChat.setOnClickListener(v -> {
            Toast.makeText(this, "AI Chat - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        highlightNavItem(navPharma);
    }

    private void highlightNavItem(LinearLayout selectedItem) {
        LinearLayout[] navItems = {
                findViewById(R.id.nav_home),
                findViewById(R.id.nav_donors),
                findViewById(R.id.nav_urgent),
                findViewById(R.id.nav_pharma),
                findViewById(R.id.nav_chat)
        };

        for (LinearLayout navItem : navItems) {
            if (navItem != null && navItem.getChildCount() > 1) {
                TextView textView = (TextView) navItem.getChildAt(1);
                textView.setTextColor(getColor(R.color.textSecondary));
            }
        }

        if (selectedItem != null && selectedItem.getChildCount() > 1) {
            TextView textView = (TextView) selectedItem.getChildAt(1);
            textView.setTextColor(getColor(R.color.primaryColor));
        }
    }

    // Pharmacy model class
    private static class Pharmacy {
        String name;
        String location;
        String hours;
        String rating;
        String phone;
        String mapLink;

        Pharmacy(String name, String location, String hours, String rating, String phone, String mapLink) {
            this.name = name;
            this.location = location;
            this.hours = hours;
            this.rating = rating;
            this.phone = phone;
            this.mapLink = mapLink;
        }
    }

    // Pharmacy Adapter
    private class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.ViewHolder> {

        private List<Pharmacy> pharmacies;

        PharmacyAdapter(List<Pharmacy> pharmacies) {
            this.pharmacies = pharmacies;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_pharmacy, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Pharmacy pharmacy = pharmacies.get(position);
            holder.bind(pharmacy);
        }

        @Override
        public int getItemCount() {
            return pharmacies.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvLocation, tvHours, tvRating, tvPhone;
            MaterialButton btnCall, btnLocation;
            MaterialCardView cardView;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvPharmacyName);
                tvLocation = itemView.findViewById(R.id.tvLocation);
                tvHours = itemView.findViewById(R.id.tvHours);
                tvRating = itemView.findViewById(R.id.tvRating);
                tvPhone = itemView.findViewById(R.id.tvPhone);
                btnCall = itemView.findViewById(R.id.btnCall);
                btnLocation = itemView.findViewById(R.id.btnLocation);
                cardView = itemView.findViewById(R.id.cardView);
            }

            void bind(Pharmacy pharmacy) {
                tvName.setText(pharmacy.name);
                tvLocation.setText(pharmacy.location);
                tvHours.setText(pharmacy.hours);
                tvRating.setText(pharmacy.rating);
                tvPhone.setText(pharmacy.phone);

                btnCall.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + pharmacy.phone));
                    startActivity(intent);
                });

                btnLocation.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(pharmacy.mapLink));
                    startActivity(intent);
                });
            }
        }
    }
}