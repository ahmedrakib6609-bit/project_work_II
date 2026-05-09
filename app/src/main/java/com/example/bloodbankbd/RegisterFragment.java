package com.example.bloodbankbd;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {

    private TextInputEditText etFullName, etMobile, etEmail, etPassword;
    private MaterialButton btnCreateAccount, btnAPositive, btnANegative, btnBPositive, btnBNegative;
    private MaterialButton btnOPositive, btnONegative, btnABPositive, btnABNegative;
    private TextView tvAlreadyMember;
    private String selectedBloodGroup = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize views
        etFullName = view.findViewById(R.id.etFullName);
        etMobile = view.findViewById(R.id.etMobile);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        tvAlreadyMember = view.findViewById(R.id.tvAlreadyMember);

        // Blood group buttons
        btnAPositive = view.findViewById(R.id.btnAPositive);
        btnANegative = view.findViewById(R.id.btnANegative);
        btnBPositive = view.findViewById(R.id.btnBPositive);
        btnBNegative = view.findViewById(R.id.btnBNegative);
        btnOPositive = view.findViewById(R.id.btnOPositive);
        btnONegative = view.findViewById(R.id.btnONegative);
        btnABPositive = view.findViewById(R.id.btnABPositive);
        btnABNegative = view.findViewById(R.id.btnABNegative);

        // Blood group selection listener
        View.OnClickListener bloodGroupClickListener = v -> {
            resetBloodGroupButtons();

            MaterialButton selectedButton = (MaterialButton) v;
            selectedButton.setBackgroundTintList(
                    getResources().getColorStateList(R.color.primaryColor)
            );
            selectedButton.setTextColor(getResources().getColor(R.color.white));

            selectedBloodGroup = selectedButton.getText().toString();
        };

        // Set click listeners for all blood group buttons
        btnAPositive.setOnClickListener(bloodGroupClickListener);
        btnANegative.setOnClickListener(bloodGroupClickListener);
        btnBPositive.setOnClickListener(bloodGroupClickListener);
        btnBNegative.setOnClickListener(bloodGroupClickListener);
        btnOPositive.setOnClickListener(bloodGroupClickListener);
        btnONegative.setOnClickListener(bloodGroupClickListener);
        btnABPositive.setOnClickListener(bloodGroupClickListener);
        btnABNegative.setOnClickListener(bloodGroupClickListener);

        // Create account button
        btnCreateAccount.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (fullName.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (selectedBloodGroup.isEmpty()) {
                Toast.makeText(getActivity(), "Please select blood group", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(),
                        "Account created for " + fullName + " (" + selectedBloodGroup + ")",
                        Toast.LENGTH_SHORT).show();

                // Navigate to HomeActivity
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        // Already have account text - ফিক্স করা কোড
        tvAlreadyMember.setOnClickListener(v -> {
            if (getActivity() != null && getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).switchToTab(0); // 0 মানে SIGN IN ট্যাব
            }
        });

        return view;
    }

    private void resetBloodGroupButtons() {
        int primaryLight = getResources().getColor(R.color.primaryLight);
        int white = getResources().getColor(R.color.white);

        MaterialButton[] buttons = {
                btnAPositive, btnANegative, btnBPositive, btnBNegative,
                btnOPositive, btnONegative, btnABPositive, btnABNegative
        };

        for (MaterialButton button : buttons) {
            if (button != null) {
                button.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(primaryLight)
                );
                button.setTextColor(white);
            }
        }
    }
}