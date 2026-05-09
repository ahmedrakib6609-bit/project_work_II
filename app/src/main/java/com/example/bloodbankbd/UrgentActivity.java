package com.example.bloodbankbd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class UrgentActivity extends AppCompatActivity {

    private RecyclerView urgentRequestsRecyclerView;
    private UrgentRequestAdapter adapter;
    private List<UrgentRequest> requestList;
    private View fabAddRequest;
    private ViewGroup emptyStateView;

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

            // Setup RecyclerView
            urgentRequestsRecyclerView = findViewById(R.id.urgentRequestsRecyclerView);
            emptyStateView = findViewById(R.id.emptyStateView);

            // Initialize request list with sample data
            requestList = new ArrayList<>();
            loadSampleRequests();

            // Setup adapter
            adapter = new UrgentRequestAdapter(requestList);
            urgentRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            urgentRequestsRecyclerView.setAdapter(adapter);

            // Check if list is empty
            updateEmptyState();

            // FAB for adding new request
            fabAddRequest = findViewById(R.id.fabAddRequest);
            if (fabAddRequest != null) {
                fabAddRequest.setOnClickListener(v -> showAddRequestBottomSheet());
            }

            // Setup bottom navigation
            setupBottomNavigation();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        } else {
            urgentRequestsRecyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    private void showAddRequestBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_add_request, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Initialize views in bottom sheet
        TextInputEditText etHospitalName = bottomSheetView.findViewById(R.id.etHospitalName);
        TextInputEditText etLocation = bottomSheetView.findViewById(R.id.etLocation);
        ChipGroup chipGroupBloodGroup = bottomSheetView.findViewById(R.id.chipGroupBloodGroup);
        TextInputEditText etQuantity = bottomSheetView.findViewById(R.id.etQuantity);
        TextInputEditText etContactNumber = bottomSheetView.findViewById(R.id.etContactNumber);
        TextInputEditText etAdditionalInfo = bottomSheetView.findViewById(R.id.etAdditionalInfo);
        MaterialButton btnSubmitRequest = bottomSheetView.findViewById(R.id.btnSubmitRequest);
        MaterialButton btnCancel = bottomSheetView.findViewById(R.id.btnCancel);

        // Variable to store selected blood group
        final String[] selectedBloodGroup = {"A+"};

        // Setup chip group listener
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

            // Add new request to list
            UrgentRequest newRequest = new UrgentRequest(
                    hospitalName,
                    location,
                    selectedBloodGroup[0],
                    quantity + " bags needed - " + additionalInfo,
                    "Just now",
                    contactNumber,
                    true
            );

            requestList.add(0, newRequest); // Add at the beginning
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
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UrgentActivity.this, HomeActivity.class));
                finish();
                highlightNavItem(v);
            });
        }

        if (navDonors != null) {
            navDonors.setOnClickListener(v -> {
                Toast.makeText(this, "Donors", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UrgentActivity.this, DonorsActivity.class));
                finish();
                highlightNavItem(v);
            });
        }

        if (navUrgent != null) {
            navUrgent.setOnClickListener(v -> {
                Toast.makeText(this, "Already on Urgent page", Toast.LENGTH_SHORT).show();
                highlightNavItem(v);
            });
        }

        if (navPharma != null) {
            navPharma.setOnClickListener(v -> {
                Toast.makeText(this, "Pharmacy", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UrgentActivity.this, PharmaActivity.class));
                finish();
                highlightNavItem(v);
            });
        }

        if (navChat != null) {
            navChat.setOnClickListener(v -> {
                Toast.makeText(this, "AI Chat", Toast.LENGTH_SHORT).show();
                highlightNavItem(v);
            });
        }

        // Initially highlight urgent
        if (navUrgent != null) {
            highlightNavItem(navUrgent);
        }
    }

    private void highlightNavItem(View selectedItem) {
        View[] navItems = {
                findViewById(R.id.nav_home),
                findViewById(R.id.nav_donors),
                findViewById(R.id.nav_urgent),
                findViewById(R.id.nav_pharma),
                findViewById(R.id.nav_chat)
        };

        // Reset all items
        for (View navItem : navItems) {
            if (navItem != null && navItem instanceof ViewGroup) {
                ViewGroup navItemGroup = (ViewGroup) navItem;

                // First child should be TextView (emoji)
                if (navItemGroup.getChildCount() > 1) {
                    View textView = navItemGroup.getChildAt(1);
                    if (textView instanceof TextView) {
                        TextView text = (TextView) textView;
                        text.setTextColor(getResources().getColor(R.color.textSecondary));
                    }
                }
            }
        }

        // Highlight selected item
        if (selectedItem != null && selectedItem instanceof ViewGroup) {
            ViewGroup selectedItemGroup = (ViewGroup) selectedItem;

            if (selectedItemGroup.getChildCount() > 1) {
                View textView = selectedItemGroup.getChildAt(1);
                if (textView instanceof TextView) {
                    TextView text = (TextView) textView;
                    text.setTextColor(getResources().getColor(R.color.primaryColor));
                }
            }
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

            // Set verified badge visibility
            if (request.isVerified) {
                holder.verifiedBadge.setVisibility(View.VISIBLE);
            } else {
                holder.verifiedBadge.setVisibility(View.GONE);
            }

            // Call button click
            holder.btnCall.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + request.contactNumber));
                startActivity(intent);
            });

            // SMS button click
            holder.btnSms.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + request.contactNumber));
                startActivity(intent);
            });

            // Share button click
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