package com.example.bloodbankbd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivBack, ivProfileImage;
    private TextView tvName, tvMobile, tvEmail, tvBloodGroup, tvLocation, tvDonationCount;
    private MaterialButton btnEditProfile, btnLogout, btnViewCart;
    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    // SharedPreferences for saving profile image
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("ProfileData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Initialize views
        ivBack = findViewById(R.id.ivBack);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvEmail = findViewById(R.id.tvEmail);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        tvLocation = findViewById(R.id.tvLocation);
        tvDonationCount = findViewById(R.id.tvDonationCount);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnViewCart = findViewById(R.id.btnViewCart);
        rvCartItems = findViewById(R.id.rvCartItems);

        // Set user data (Hardcoded for frontend)
        tvName.setText("Rakib Ahmed");
        tvMobile.setText("017XXXXXXXX");
        tvEmail.setText("rakib@gmail.com");
        tvBloodGroup.setText("O+");
        tvLocation.setText("Dhaka, Bangladesh");
        tvDonationCount.setText("15 times");

        // Load saved profile image
        loadSavedProfileImage();

        // Back button click
        ivBack.setOnClickListener(v -> finish());

        // Profile image click - Open Gallery
        ivProfileImage.setOnClickListener(v -> {
            openGallery();
        });

        // Edit Profile button click
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profile (Coming Soon)", Toast.LENGTH_SHORT).show();
        });

        // Logout button click
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        // View Cart button click
        btnViewCart.setOnClickListener(v -> {
            Toast.makeText(this, "View Cart - " + cartItems.size() + " items", Toast.LENGTH_SHORT).show();
        });

        // Load cart items
        loadCartItems();

        // Setup RecyclerView for cart items
        cartAdapter = new CartAdapter(cartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartAdapter);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Save image URI to SharedPreferences
            editor.putString("profile_image_uri", imageUri.toString());
            editor.apply();

            // Display the selected image
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivProfileImage.setImageBitmap(bitmap);
                ivProfileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivProfileImage.setPadding(0, 0, 0, 0);
                ivProfileImage.setBackground(null);
                Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadSavedProfileImage() {
        String savedImageUri = sharedPreferences.getString("profile_image_uri", null);
        if (savedImageUri != null) {
            try {
                Uri imageUri = Uri.parse(savedImageUri);
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivProfileImage.setImageBitmap(bitmap);
                ivProfileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivProfileImage.setPadding(0, 0, 0, 0);
                ivProfileImage.setBackground(null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCartItems() {
        cartItems = new ArrayList<>();

        // Sample cart items (from Pharma page)
        cartItems.add(new CartItem(R.drawable.ic_medicine, "Napa Extra", "৳ 15", 1));
        cartItems.add(new CartItem(R.drawable.ic_medicine, "Seclo 20mg", "৳ 12", 2));
        cartItems.add(new CartItem(R.drawable.ic_medicine, "Maxpro 20mg", "৳ 14", 1));
    }

    // Cart Item Model
    private static class CartItem {
        int imageRes;
        String name;
        String price;
        int quantity;

        CartItem(int imageRes, String name, String price, int quantity) {
            this.imageRes = imageRes;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }

    // Cart Adapter
    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

        private List<CartItem> items;

        CartAdapter(List<CartItem> items) {
            this.items = items;
        }

        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_cart, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
            CartItem item = items.get(position);
            holder.ivImage.setImageResource(item.imageRes);
            holder.tvName.setText(item.name);
            holder.tvPrice.setText(item.price);
            holder.tvQuantity.setText("Qty: " + item.quantity);

            holder.btnRemove.setOnClickListener(v -> {
                Toast.makeText(ProfileActivity.this, "Removed: " + item.name, Toast.LENGTH_SHORT).show();
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;
            TextView tvName, tvPrice, tvQuantity;
            MaterialButton btnRemove;

            ViewHolder(View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivCartImage);
                tvName = itemView.findViewById(R.id.tvCartName);
                tvPrice = itemView.findViewById(R.id.tvCartPrice);
                tvQuantity = itemView.findViewById(R.id.tvCartQuantity);
                btnRemove = itemView.findViewById(R.id.btnRemove);
            }
        }
    }
}