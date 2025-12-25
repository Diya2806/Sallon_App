package Model;

public class ShopFeedback_model {
    private String customerName;
    private String feedback;
    private String imageResId;
    private float rating;

    public ShopFeedback_model(String customerName, String feedback, String imageResId, float rating) {
        this.customerName = customerName;
        this.feedback = feedback;
        this.imageResId = imageResId;
        this.rating = rating;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
