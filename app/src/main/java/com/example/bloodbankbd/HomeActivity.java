package com.example.bloodbankbd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);

        try {
            // Initialize profile button
            ImageView btnProfile = findViewById(R.id.btnProfile);
            if (btnProfile != null) {
                btnProfile.setOnClickListener(v -> {
                    Toast.makeText(this, "Profile Menu", Toast.LENGTH_SHORT).show();
                });
            }

            // 🔴 Ribbon Donors Button click listener
            LinearLayout ribbonDonorsButton = findViewById(R.id.ribbonDonorsButton);
            if (ribbonDonorsButton != null) {
                ribbonDonorsButton.setOnClickListener(v -> {
                    Toast.makeText(HomeActivity.this, "Opening Verified Donors List", Toast.LENGTH_SHORT).show();
                    // You can add navigation to donors activity here
                    // Example:
                    // Intent intent = new Intent(HomeActivity.this, DonorsActivity.class);
                    // startActivity(intent);
                });
            }

            // Initialize custom bottom navigation
            setupBottomNavigation();

            // Blood group buttons
            setupBloodGroupButtons();

            // Become a donor button
            MaterialButton btnBecomeDonor = findViewById(R.id.btnBecomeDonor);
            if (btnBecomeDonor != null) {
                btnBecomeDonor.setOnClickListener(v -> {
                    Toast.makeText(HomeActivity.this, "Join as a Life Saver", Toast.LENGTH_SHORT).show();
                });
            }

            // Recent Life Savers click listeners
            setupLifeSaversClickListeners();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
                highlightNavItem(v);
            });
        }

        if (navDonors != null) {
            navDonors.setOnClickListener(v -> {
                Toast.makeText(this, "Donors", Toast.LENGTH_SHORT).show();
                highlightNavItem(v);
                // Open DonorsActivity
                Intent intent = new Intent(HomeActivity.this, DonorsActivity.class);
                startActivity(intent);
            });
        }

        if (navUrgent != null) {
            navUrgent.setOnClickListener(v -> {
                Toast.makeText(this, "Urgent", Toast.LENGTH_SHORT).show();
                highlightNavItem(v);
                // Open UrgentActivity
                Intent intent = new Intent(HomeActivity.this, UrgentActivity.class);
                startActivity(intent);
            });
        }

        if (navPharma != null) {
            navPharma.setOnClickListener(v -> {
                Toast.makeText(this, "Pharma", Toast.LENGTH_SHORT).show();
                highlightNavItem(v);
                // Open PharmaActivity
                Intent intent = new Intent(HomeActivity.this, PharmaActivity.class);
                startActivity(intent);
            });
        }

        if (navChat != null) {
            navChat.setOnClickListener(v -> {
                Toast.makeText(this, "AI Chat", Toast.LENGTH_SHORT).show();
                highlightNavItem(v);
            });
        }

        // Initially highlight home
        if (navHome != null) {
            highlightNavItem(navHome);
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

                // First child should be ImageView
                if (navItemGroup.getChildCount() > 0) {
                    View iconView = navItemGroup.getChildAt(0);
                    if (iconView instanceof ImageView) {
                        ImageView icon = (ImageView) iconView;
                        icon.setColorFilter(getResources().getColor(R.color.textSecondary));
                    }
                }

                // Second child should be TextView
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

            // First child should be ImageView
            if (selectedItemGroup.getChildCount() > 0) {
                View iconView = selectedItemGroup.getChildAt(0);
                if (iconView instanceof ImageView) {
                    ImageView icon = (ImageView) iconView;
                    icon.setColorFilter(getResources().getColor(R.color.primaryColor));
                }
            }

            // Second child should be TextView
            if (selectedItemGroup.getChildCount() > 1) {
                View textView = selectedItemGroup.getChildAt(1);
                if (textView instanceof TextView) {
                    TextView text = (TextView) textView;
                    text.setTextColor(getResources().getColor(R.color.primaryColor));
                }
            }
        }
    }

    private void setupBloodGroupButtons() {
        MaterialButton btnAPos = findViewById(R.id.btnAPos);
        MaterialButton btnANeg = findViewById(R.id.btnANeg);
        MaterialButton btnBPos = findViewById(R.id.btnBPos);
        MaterialButton btnBNeg = findViewById(R.id.btnBNeg);
        MaterialButton btnOPos = findViewById(R.id.btnOPos);
        MaterialButton btnONeg = findViewById(R.id.btnONeg);
        MaterialButton btnABPos = findViewById(R.id.btnABPos);
        MaterialButton btnABNeg = findViewById(R.id.btnABNeg);

        MaterialButton[] bloodGroupButtons = {btnAPos, btnANeg, btnBPos, btnBNeg, btnOPos, btnONeg, btnABPos, btnABNeg};

        for (MaterialButton button : bloodGroupButtons) {
            if (button != null) {
                button.setOnClickListener(v -> {
                    String bloodGroup = button.getText().toString();
                    Toast.makeText(HomeActivity.this, "Searching for " + bloodGroup + " donors", Toast.LENGTH_SHORT).show();
                    // You can add blood group search functionality here
                });
            }
        }
    }

    private void setupLifeSaversClickListeners() {
        // Find all life saver containers by their position in layout
        // Life Saver 1
        LinearLayout lifeSaver1 = findViewById(android.R.id.list);
        if (lifeSaver1 == null) {
            // Alternative: Find by parent and index
            ViewGroup recentLifeSaversContainer = findViewById(R.id.ribbonDonorsButton);
            if (recentLifeSaversContainer != null && recentLifeSaversContainer.getParent() instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) recentLifeSaversContainer.getParent();
                int startIndex = 0;
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View child = parent.getChildAt(i);
                    if (child instanceof LinearLayout && child.getId() == R.id.ribbonDonorsButton) {
                        startIndex = i + 1;
                        break;
                    }
                }

                // Set click listeners for each life saver item
                for (int i = startIndex; i < parent.getChildCount(); i++) {
                    View child = parent.getChildAt(i);
                    if (child instanceof LinearLayout && child.hasOnClickListeners()) {
                        child.setOnClickListener(v -> {
                            // Extract donor name from the layout
                            if (child instanceof ViewGroup) {
                                ViewGroup group = (ViewGroup) child;
                                for (int j = 0; j < group.getChildCount(); j++) {
                                    View innerChild = group.getChildAt(j);
                                    if (innerChild instanceof ViewGroup) {
                                        ViewGroup innerGroup = (ViewGroup) innerChild;
                                        for (int k = 0; k < innerGroup.getChildCount(); k++) {
                                            View textChild = innerGroup.getChildAt(k);
                                            if (textChild instanceof TextView) {
                                                TextView textView = (TextView) textChild;
                                                if (textView.getText().toString().matches(".*[A-Za-z].*")) {
                                                    Toast.makeText(HomeActivity.this,
                                                            "Viewing " + textView.getText().toString() + "'s profile",
                                                            Toast.LENGTH_SHORT).show();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    // Helper method to make life saver items clickable
    private void setLifeSaverClickListener(View lifeSaverView, String donorName) {
        if (lifeSaverView != null) {
            lifeSaverView.setOnClickListener(v -> {
                Toast.makeText(HomeActivity.this,
                        "Viewing " + donorName + "'s profile",
                        Toast.LENGTH_SHORT).show();
            });
        }
    }
}