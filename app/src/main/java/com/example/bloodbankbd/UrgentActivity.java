package com.example.bloodbankbd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class UrgentActivity extends AppCompatActivity {

    private RecyclerView urgentRequestsRecyclerView;
    private UrgentRequestAdapter adapter;
    private List<UrgentRequest> requestList;
    private ViewGroup emptyStateView;
    private TabLayout tabLayout;
    private LinearLayout sosContainer, ambulanceContainer, addRequestContainer;
    private MaterialButton btnSOS, btnAmbulance, btnAddRequest;
    private TextView tvRequestCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent);

        try {
            // Back button setup
            ImageView btnBack = findViewById(R.id.btnBack);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> finish());
            }

            // Page title setup
            TextView tvTitle = findViewById(R.id.tvTitle);
            if (tvTitle != null) {
                tvTitle.setText("Urgent Blood Requests");
            }

            // Initialize TabLayout and Containers
            tabLayout = findViewById(R.id.tabLayout);
            sosContainer = findViewById(R.id.sosContainer);
            ambulanceContainer = findViewById(R.id.ambulanceContainer);
            addRequestContainer = findViewById(R.id.addRequestContainer);

            // Setup Tab Layout
            setupTabLayout();

            // Setup Add Request Tab
            urgentRequestsRecyclerView = findViewById(R.id.urgentRequestsRecyclerView);
            emptyStateView = findViewById(R.id.emptyStateView);
            tvRequestCount = findViewById(R.id.tvRequestCount);
            btnAddRequest = findViewById(R.id.btnAddRequest);

            // Initialize request list with sample data
            requestList = new ArrayList<>();
            loadSampleRequests();

            // Setup adapter
            adapter = new UrgentRequestAdapter(requestList);
            urgentRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            urgentRequestsRecyclerView.setAdapter(adapter);

            // Check if list is empty
            updateEmptyState();

            // নরমাল Add Request Button
            if (btnAddRequest != null) {
                btnAddRequest.setOnClickListener(v -> showAddRequestBottomSheet());
            }

            // Setup SOS Tab
            btnSOS = findViewById(R.id.btnSOS);
            if (btnSOS != null) {
                btnSOS.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:999"));
                    startActivity(intent);
                    Toast.makeText(this, "Emergency SOS - Calling 999", Toast.LENGTH_SHORT).show();
                });
            }

            // Setup Ambulance Tab
            btnAmbulance = findViewById(R.id.btnAmbulance);
            if (btnAmbulance != null) {
                btnAmbulance.setOnClickListener(v -> {
                    showAmbulanceDialog();
                });
            }

            // Setup bottom navigation
            setupBottomNavigation();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                sosContainer.setVisibility(View.GONE);
                ambulanceContainer.setVisibility(View.GONE);
                addRequestContainer.setVisibility(View.GONE);

                if (position == 0) {
                    addRequestContainer.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    sosContainer.setVisibility(View.VISIBLE);
                } else {
                    ambulanceContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void showAmbulanceDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🚑 Ambulance Services");
        builder.setMessage("Select your location to call an ambulance:");

        final String[][] ambulanceNumbers = {
                {"🩸 Central Ambulance (24/7)", "999"},
                {"🩸 Dhaka - Central Ambulance", "01777777777"},
                {"🩸 Chittagong - Red Crescent", "01812345678"},
                {"🩸 Khulna - General Ambulance", "01912345678"},
                {"🩸 Rajshahi - Emergency Service", "01512345678"},
                {"🩸 Sylhet - Life Saver Ambulance", "01612345678"},
                {"🩸 Barisal - Ambulance Service", "01787654321"},
                {"🩸 Rangpur - Emergency Ambulance", "01987654321"}
        };

        String[] options = new String[ambulanceNumbers.length];
        for (int i = 0; i < ambulanceNumbers.length; i++) {
            options[i] = ambulanceNumbers[i][0];
        }

        builder.setItems(options, (dialog, which) -> {
            String phoneNumber = ambulanceNumbers[which][1];
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
            Toast.makeText(this, "Calling " + ambulanceNumbers[which][0], Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void loadSampleRequests() {
        requestList.add(new UrgentRequest(
                "City Hospital",
                "Dhaka",
                "A+",
                "2 bags needed immediately",
                "2 hours ago",
                "01712345678",
                true
        ));

        requestList.add(new UrgentRequest(
                "Square Hospital",
                "Dhaka",
                "O-",
                "Emergency surgery - 3 bags required",
                "30 minutes ago",
                "01876543210",
                true
        ));

        requestList.add(new UrgentRequest(
                "Combined Military Hospital",
                "Chittagong",
                "B+",
                "Accident victim needs blood",
                "1 hour ago",
                "01911223344",
                false
        ));

        requestList.add(new UrgentRequest(
                "Dhaka Medical College",
                "Dhaka",
                "AB+",
                "Thalassemia patient needs blood",
                "3 hours ago",
                "01698765432",
                true
        ));
    }

    private void updateEmptyState() {
        if (requestList.isEmpty()) {
            urgentRequestsRecyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
            if (tvRequestCount != null) tvRequestCount.setText("0 requests");
        } else {
            urgentRequestsRecyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
            if (tvRequestCount != null) tvRequestCount.setText(requestList.size() + " requests");
        }
    }

    private void showAddRequestBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_add_request, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextInputEditText etHospitalName = bottomSheetView.findViewById(R.id.etHospitalName);
        TextInputEditText etLocation = bottomSheetView.findViewById(R.id.etLocation);
        ChipGroup chipGroupBloodGroup = bottomSheetView.findViewById(R.id.chipGroupBloodGroup);
        TextInputEditText etQuantity = bottomSheetView.findViewById(R.id.etQuantity);
        TextInputEditText etContactNumber = bottomSheetView.findViewById(R.id.etContactNumber);
        TextInputEditText etAdditionalInfo = bottomSheetView.findViewById(R.id.etAdditionalInfo);
        MaterialButton btnSubmitRequest = bottomSheetView.findViewById(R.id.btnSubmitRequest);
        MaterialButton btnCancel = bottomSheetView.findViewById(R.id.btnCancel);

        final String[] selectedBloodGroup = {"A+"};

        chipGroupBloodGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.size() > 0) {
                int checkedId = checkedIds.get(0);
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    selectedBloodGroup[0] = chip.getText().toString();
                }
            }
        });

        btnSubmitRequest.setOnClickListener(v -> {
            String hospitalName = etHospitalName.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String quantity = etQuantity.getText().toString().trim();
            String contactNumber = etContactNumber.getText().toString().trim();
            String additionalInfo = etAdditionalInfo.getText().toString().trim();

            if (hospitalName.isEmpty()) {
                etHospitalName.setError("Hospital name is required");
                return;
            }

            if (location.isEmpty()) {
                etLocation.setError("Location is required");
                return;
            }

            if (quantity.isEmpty()) {
                etQuantity.setError("Quantity is required");
                return;
            }

            if (contactNumber.isEmpty()) {
                etContactNumber.setError("Contact number is required");
                return;
            }

            UrgentRequest newRequest = new UrgentRequest(
                    hospitalName,
                    location,
                    selectedBloodGroup[0],
                    quantity + " bags needed - " + additionalInfo,
                    "Just now",
                    contactNumber,
                    true
            );

            requestList.add(0, newRequest);
            adapter.notifyItemInserted(0);
            urgentRequestsRecyclerView.scrollToPosition(0);
            updateEmptyState();

            Toast.makeText(this, "Request posted successfully", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    private void setupBottomNavigation() {
        View navHome = findViewById(R.id.nav_home);
        View navDonors = findViewById(R.id.nav_donors);
        View navUrgent = findViewById(R.id.nav_urgent);
        View navPharma = findViewById(R.id.nav_pharma);
        View navChat = findViewById(R.id.nav_chat);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(UrgentActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            });
        }

        if (navDonors != null) {
            navDonors.setOnClickListener(v -> {
                Intent intent = new Intent(UrgentActivity.this, DonorsActivity.class);
                startActivity(intent);
                finish();
            });
        }

        if (navUrgent != null) {
            navUrgent.setOnClickListener(v -> {
                Toast.makeText(this, "Already on Urgent page", Toast.LENGTH_SHORT).show();
            });
        }

        if (navPharma != null) {
            navPharma.setOnClickListener(v -> {
                Intent intent = new Intent(UrgentActivity.this, PharmaActivity.class);
                startActivity(intent);
                finish();
            });
        }

        if (navChat != null) {
            navChat.setOnClickListener(v -> {
                Toast.makeText(this, "AI Chat - Coming Soon", Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Inner class for Urgent Request model
    private static class UrgentRequest {
        String hospitalName;
        String location;
        String bloodGroup;
        String description;
        String timeAgo;
        String contactNumber;
        boolean isVerified;

        UrgentRequest(String hospitalName, String location, String bloodGroup,
                      String description, String timeAgo, String contactNumber, boolean isVerified) {
            this.hospitalName = hospitalName;
            this.location = location;
            this.bloodGroup = bloodGroup;
            this.description = description;
            this.timeAgo = timeAgo;
            this.contactNumber = contactNumber;
            this.isVerified = isVerified;
        }
    }

    // Adapter class for RecyclerView
    private class UrgentRequestAdapter extends RecyclerView.Adapter<UrgentRequestAdapter.ViewHolder> {

        private List<UrgentRequest> requests;

        UrgentRequestAdapter(List<UrgentRequest> requests) {
            this.requests = requests;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_urgent_request, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            UrgentRequest request = requests.get(position);

            holder.tvHospitalName.setText(request.hospitalName);
            holder.tvLocation.setText(request.location);
            holder.tvBloodGroup.setText(request.bloodGroup);
            holder.tvDescription.setText(request.description);
            holder.tvTimeAgo.setText(request.timeAgo);

            if (request.isVerified) {
                holder.verifiedBadge.setVisibility(View.VISIBLE);
            } else {
                holder.verifiedBadge.setVisibility(View.GONE);
            }

            holder.btnCall.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + request.contactNumber));
                startActivity(intent);
            });

            holder.btnSms.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + request.contactNumber));
                startActivity(intent);
            });

            holder.btnShare.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Urgent Blood Request");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Urgent need of " + request.bloodGroup + " blood at " +
                                request.hospitalName + ", " + request.location +
                                ". Contact: " + request.contactNumber);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            });
        }

        @Override
        public int getItemCount() {
            return requests.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvHospitalName, tvLocation, tvBloodGroup, tvDescription, tvTimeAgo;
            MaterialButton btnCall, btnSms, btnShare;
            View verifiedBadge;
            MaterialCardView cardView;

            ViewHolder(View itemView) {
                super(itemView);
                tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
                tvLocation = itemView.findViewById(R.id.tvLocation);
                tvBloodGroup = itemView.findViewById(R.id.tvBloodGroup);
                tvDescription = itemView.findViewById(R.id.tvDescription);
                tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
                btnCall = itemView.findViewById(R.id.btnCall);
                btnSms = itemView.findViewById(R.id.btnSms);
                btnShare = itemView.findViewById(R.id.btnShare);
                verifiedBadge = itemView.findViewById(R.id.verifiedBadge);
                cardView = itemView.findViewById(R.id.cardView);
            }
        }
    }
}
