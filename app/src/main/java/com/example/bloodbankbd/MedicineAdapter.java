package com.example.bloodbankbd;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private List<Medicine> medicines;
    private OnMedicineClickListener listener;
    private String searchQuery = "";

    public interface OnMedicineClickListener {
        void onMedicineClick(Medicine medicine);
    }

    public MedicineAdapter(List<Medicine> medicines, OnMedicineClickListener listener) {
        this.medicines = new ArrayList<>(medicines);
        this.listener = listener;
    }

    public void updateList(List<Medicine> newList) {
        this.medicines.clear();
        this.medicines.addAll(newList);
        notifyDataSetChanged();
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        notifyDataSetChanged();
    }

    private SpannableString getHighlightedText(String text, String query) {
        if (query == null || query.isEmpty()) {
            return new SpannableString(text);
        }

        SpannableString spannableString = new SpannableString(text);
        String lowerText = text.toLowerCase(Locale.getDefault());
        String lowerQuery = query.toLowerCase(Locale.getDefault());

        int startIndex = lowerText.indexOf(lowerQuery);
        while (startIndex >= 0) {
            int endIndex = startIndex + lowerQuery.length();
            spannableString.setSpan(
                    new BackgroundColorSpan(0xFFFFFF00),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            startIndex = lowerText.indexOf(lowerQuery, endIndex);
        }
        return spannableString;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.bind(medicine);
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivMedicineImage;
        private TextView tvName, tvCompany, tvDescription, tvPrice, tvStockStatus, tvAddToCart;

        ViewHolder(View itemView) {
            super(itemView);
            ivMedicineImage = itemView.findViewById(R.id.ivMedicineImage);
            tvName = itemView.findViewById(R.id.tvMedicineName);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStockStatus = itemView.findViewById(R.id.tvStockStatus);
            tvAddToCart = itemView.findViewById(R.id.tvAddToCart);
        }

        void bind(Medicine medicine) {
            // Highlight text if search query exists
            if (searchQuery != null && !searchQuery.isEmpty()) {
                tvName.setText(getHighlightedText(medicine.getName(), searchQuery));
            } else {
                tvName.setText(medicine.getName());
            }

            tvCompany.setText(medicine.getCompany());
            tvDescription.setText(medicine.getDescription());
            tvPrice.setText("৳ " + medicine.getPrice());
            tvStockStatus.setText(medicine.getStockStatus());
            ivMedicineImage.setImageResource(medicine.getImageResId());

            // Add To Cart TextView click listener
            tvAddToCart.setOnClickListener(v -> {
                Toast.makeText(v.getContext(),
                        "Added to Cart: " + medicine.getName(),
                        Toast.LENGTH_SHORT).show();

                if (listener != null) {
                    listener.onMedicineClick(medicine);
                }
            });
        }
    }
}