package com.example.bloodbankbd;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AuthActivity extends AppCompatActivity {

    private ViewPager2 viewPager;  // private রাখুন
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        AuthPagerAdapter pagerAdapter = new AuthPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("SIGN IN");
            } else {
                tab.setText("JOIN NOW");
            }
        }).attach();
    }

    // নতুন public method যোগ করুন - ট্যাব পরিবর্তনের জন্য
    public void switchToTab(int position) {
        if (viewPager != null) {
            viewPager.setCurrentItem(position, true);
        }
    }
}