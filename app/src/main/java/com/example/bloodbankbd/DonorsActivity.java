package com.example.bloodbankbd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class DonorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors);

        try {
            // Back button
            TextView btnBack = findViewById(R.id.btnBack);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> {
                    finish();
                });
            }

            // Setup bottom navigation
            setupBottomNavigation();

            // Setup donor buttons functionality (CALL and SMS)
            setupDonorButtons();

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
                Intent intent = new Intent(DonorsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                highlightNavItem(v);
            });
        }

        if (navDonors != null) {
            navDonors.setOnClickListener(v -> {
                Toast.makeText(this, "Already on Donors page", Toast.LENGTH_SHORT).show();
                highlightNavItem(v);
            });
        }

        if (navUrgent != null) {
            navUrgent.setOnClickListener(v -> {
                Toast.makeText(this, "Opening Urgent page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DonorsActivity.this, UrgentActivity.class);
                startActivity(intent);
                finish();
                highlightNavItem(v);
            });
        }

        if (navPharma != null) {
            navPharma.setOnClickListener(v -> {
                Toast.makeText(this, "Pharma", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DonorsActivity.this, PharmaActivity.class);
                startActivity(intent);
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

        // Initially highlight donors
        if (navDonors != null) {
            highlightNavItem(navDonors);
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
                if (navItemGroup.getChildCount() > 0) {
                    View textView1 = navItemGroup.getChildAt(0);
                    if (textView1 instanceof TextView) {
                        TextView text = (TextView) textView1;
                        // Don't change emoji color, keep it as is
                    }
                }

                // Second child should be TextView (label)
                if (navItemGroup.getChildCount() > 1) {
                    View textView2 = navItemGroup.getChildAt(1);
                    if (textView2 instanceof TextView) {
                        TextView text = (TextView) textView2;
                        text.setTextColor(getResources().getColor(R.color.textSecondary));
                    }
                }
            }
        }

        // Highlight selected item
        if (selectedItem != null && selectedItem instanceof ViewGroup) {
            ViewGroup selectedItemGroup = (ViewGroup) selectedItem;

            // Second child should be TextView (label)
            if (selectedItemGroup.getChildCount() > 1) {
                View textView = selectedItemGroup.getChildAt(1);
                if (textView instanceof TextView) {
                    TextView text = (TextView) textView;
                    text.setTextColor(getResources().getColor(R.color.primaryColor));
                }
            }
        }
    }

    private void setupDonorButtons() {
        // Donor 1: Zahidul Hasan Imon
        setupCallButton(R.id.btnCall1, "01852134062");
        setupSmsButton(R.id.btnSms1, "01852134062");

        // Donor 2: Mushahidul Islam
        setupCallButton(R.id.btnCall2, "01324183988");
        setupSmsButton(R.id.btnSms2, "01324183988");

        // Donor 3: Rakib Ahmed
        setupCallButton(R.id.btnCall3, "01648699987");
        setupSmsButton(R.id.btnSms3, "01648699987");

        // Donor 4: Taslima Akter
        setupCallButton(R.id.btnCall4, "01712345678");
        setupSmsButton(R.id.btnSms4, "01712345678");

        // Donor 5: Karim Ullah
        setupCallButton(R.id.btnCall5, "01876543210");
        setupSmsButton(R.id.btnSms5, "01876543210");

        // Donor 6: Farzana Islam
        setupCallButton(R.id.btnCall6, "01987654321");
        setupSmsButton(R.id.btnSms6, "01987654321");

        // Donor 7: Abdur Rahim
        setupCallButton(R.id.btnCall7, "01512345678");
        setupSmsButton(R.id.btnSms7, "01512345678");

        // Donor 8: Salma Khan
        setupCallButton(R.id.btnCall8, "01698765432");
        setupSmsButton(R.id.btnSms8, "01698765432");

        // Donor 9: Rahim Uddin
        setupCallButton(R.id.btnCall9, "01787654321");
        setupSmsButton(R.id.btnSms9, "01787654321");

        // Donor 10: Fatema Begum
        setupCallButton(R.id.btnCall10, "01911223344");
        setupSmsButton(R.id.btnSms10, "01911223344");
    }

    private void setupCallButton(int buttonId, String phoneNumber) {
        View view = findViewById(buttonId);
        if (view instanceof MaterialButton) {
            MaterialButton button = (MaterialButton) view;
            button.setOnClickListener(v -> {
                // Direct call functionality
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            });
        }
    }

    private void setupSmsButton(int buttonId, String phoneNumber) {
        View view = findViewById(buttonId);
        if (view instanceof MaterialButton) {
            MaterialButton button = (MaterialButton) view;
            button.setOnClickListener(v -> {
                // SMS functionality
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + phoneNumber));
                startActivity(intent);
            });
        }
    }
}