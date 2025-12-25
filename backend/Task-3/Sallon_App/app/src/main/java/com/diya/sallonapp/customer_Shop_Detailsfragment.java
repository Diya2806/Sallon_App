    package com.diya.sallonapp;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.RatingBar;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;

    import com.bumptech.glide.Glide;

    public class customer_Shop_Detailsfragment extends Fragment {
        private ImageView shopImage;
        private TextView shopName, shopDescription, OwnerName;
        private RatingBar shopRating;
        private Button bookButton;
        private String customerId = "";


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    //        return inflater.inflate(R.layout.homefragment,container,false);
            View view = inflater.inflate(R.layout.custome_customer_shop_detils, container, false);
            shopImage = view.findViewById(R.id.detailShopImage);
            shopName = view.findViewById(R.id.detailShopName);
            shopDescription = view.findViewById(R.id.detailShopDescription);
            shopRating = view.findViewById(R.id.detailShopRating);
            bookButton = view.findViewById(R.id.btnBookAppointment);
            OwnerName = view.findViewById(R.id.DetailShopOwnerName);

            Bundle bundle = getArguments();
            if (bundle != null) {
                customerId = bundle.getString("customerId", "");
                shopName.setText(bundle.getString("shopName", "Unnamed Shop"));
                shopDescription.setText(bundle.getString("shopDetails", ""));
                shopRating.setRating(bundle.getFloat("shopRating", 0f));
                OwnerName.setText(bundle.getString("OwnerName"));
                String imageUrl = bundle.getString("shopImage");
                Glide.with(requireContext())
                        .load(imageUrl)
                        .into(shopImage);
            }
            // Optional: Book button click
            bookButton.setOnClickListener(v -> {
                String shops = shopName.getText().toString();

                Bundle bundle1 = new Bundle();
                bundle1.putString("customerId", customerId);
                bundle1.putString("ShopName", shops);
                bundle1.putString("shopId", bundle.getString("shopId"));


                Customer_Appointmentfragmnet fragment = new Customer_Appointmentfragmnet();
                fragment.setArguments(bundle1);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customermenucontainer, fragment).commit();

            });
            return view;
        }
    }
