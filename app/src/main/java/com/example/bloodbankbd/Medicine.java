package com.example.bloodbankbd;

public class Medicine {
    private String name;
    private String company;
    private String description;
    private String price;
    private String stockStatus;
    private int imageResId;

    public Medicine(String name, String company, String description, String price, String stockStatus, int imageResId) {
        this.name = name;
        this.company = company;
        this.description = description;
        this.price = price;
        this.stockStatus = stockStatus;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getStockStatus() { return stockStatus; }
    public int getImageResId() { return imageResId; }
}