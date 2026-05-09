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

public class LoginFragment extends Fragment {

    private TextInputEditText etMobile, etPassword;
    private MaterialButton btnLogin, btnJoinNow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        try {
            etMobile = view.findViewById(R.id.etMobile);
            etPassword = view.findViewById(R.id.etPassword);
            btnLogin = view.findViewById(R.id.btnLogin);
            btnJoinNow = view.findViewById(R.id.btnJoinNow);

            if (btnLogin != null) {
                btnLogin.setOnClickListener(v -> {
                    String emailOrMobile = "";
                    String password = "";

                    if (etMobile != null) emailOrMobile = etMobile.getText().toString().trim();
                    if (etPassword != null) password = etPassword.getText().toString().trim();

                    // Check if fields are empty
                    if (emailOrMobile.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter email or mobile number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if it's email or mobile
                    if (emailOrMobile.contains("@")) {
                        // EMAIL VALIDATION: Must end with @gmail.com
                        if (!emailOrMobile.endsWith("@gmail.com")) {
                            Toast.makeText(getActivity(), "Email must be @gmail.com (example@gmail.com)", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // Check if there is something before @gmail.com
                        String beforeGmail = emailOrMobile.replace("@gmail.com", "");
                        if (beforeGmail.isEmpty()) {
                            Toast.makeText(getActivity(), "Please enter a valid email (example@gmail.com)", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        // MOBILE VALIDATION: Must be 11 digits and start with 01
                        if (emailOrMobile.length() != 11) {
                            Toast.makeText(getActivity(), "Mobile number must be 11 digits", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!emailOrMobile.startsWith("01")) {
                            Toast.makeText(getActivity(), "Mobile number must start with 01", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // Check if all are digits
                        if (!emailOrMobile.matches("\\d+")) {
                            Toast.makeText(getActivity(), "Mobile number must contain only digits", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    // PASSWORD VALIDATION: Minimum 6 characters
                    if (password.length() < 6) {
                        Toast.makeText(getActivity(), "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // If all validation passed
                    Toast.makeText(getActivity(), "Login successful! Welcome to Blood Bank BD", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                });
            }

            // Join Now button to switch to registration tab
            if (btnJoinNow != null) {
                btnJoinNow.setOnClickListener(v -> {
                    if (getActivity() != null && getActivity() instanceof AuthActivity) {
                        ((AuthActivity) getActivity()).switchToTab(1);
                        Toast.makeText(getActivity(), "Create new account", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return view;
    }
}