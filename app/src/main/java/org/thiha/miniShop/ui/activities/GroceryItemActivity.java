package org.thiha.miniShop.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.thiha.miniShop.R;
import org.thiha.miniShop.model.GroceryItem;
import org.thiha.miniShop.model.Review;
import org.thiha.miniShop.ui.adapters.ReviewsAdapter;
import org.thiha.miniShop.ui.dialogs.AddReviewDialog;
import org.thiha.miniShop.utils.TrackUserTime;
import org.thiha.miniShop.utils.Utils;

import java.util.ArrayList;

public class GroceryItemActivity extends AppCompatActivity implements AddReviewDialog.AddReview {
    private static final String TAG = "GroceryItemActivity";

    @Override
    public void onAddReviewResult(Review review) {
        Log.d(TAG, "onAddReviewResult: we are adding " + review.toString());

        utils.addReview(review);
        utils.increaseUserPoint(incomingItem, 3);
        ArrayList<Review> reviews = utils.getReviewForItem(review.getGroceryItemId());
        if (null != reviews) {
            adapter.setReviews(reviews);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackUserTime.LocalBinder binder =
                    (TrackUserTime.LocalBinder) service;
            mService = binder.getService();
            isBound = true;
            mService.setItem(incomingItem);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private TrackUserTime mService;
    private boolean isBound = false;

    private TextView txtName, txtPrice, txtDescription, txtAvailability;
    private ImageView itemImage;
    private Button btnAddToCart;

    private ImageView firstFilledStar, secondFilledStar, thirdFilledStar, firstEmptyStar, secondEmptyStar, thirdEmptyStar;
    private RecyclerView reviewsRecView;

    private ReviewsAdapter adapter;

    private RelativeLayout addReviewRelLayout;

    private int currentRate = 0;

    private GroceryItem incomingItem;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_item);

        utils = new Utils(this);

        initViews();

        Intent intent = getIntent();
        try {
            incomingItem = intent.getParcelableExtra("item");
            this.currentRate = incomingItem.getRate();
            changeVisibility(currentRate);
            setViewsValues();
            utils.increaseUserPoint(incomingItem, 1);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * responsible for setting the initial values for views
     */
    private void setViewsValues() {
        Log.d(TAG, "setViewsValues: started");
        txtName.setText(incomingItem.getName());
        txtPrice.setText(incomingItem.getPrice() + "$");
        txtAvailability.setText(incomingItem.getAvailableAmount() + " number(s) available");
        txtDescription.setText(incomingItem.getDescription());

        Glide.with(this)
                .asBitmap()
                .load(incomingItem.getImageUrl())
                .into(itemImage);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.addItemToCart(incomingItem.getId());
                Intent intent = new Intent(GroceryItemActivity.this, CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        handleStarsSituation();

        adapter = new ReviewsAdapter();
        reviewsRecView.setAdapter(adapter);
        reviewsRecView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Review> reviews = utils.getReviewForItem(incomingItem.getId());
        if (null != reviews) {
            adapter.setReviews(reviews);
        }

        addReviewRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: show dialog

                AddReviewDialog addReviewDialog = new AddReviewDialog();
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", incomingItem);
                addReviewDialog.setArguments(bundle);
                addReviewDialog.show(getSupportFragmentManager(), "add review dialog");
            }
        });
    }

    private void handleStarsSituation() {
        Log.d(TAG, "handleStarsSituation: started");

        firstEmptyStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfRateHasChanged(1)) {
                    updateDatabase(1);
                    changeVisibility(1);
                    changeUserPoint(1);
                }
            }
        });

        secondEmptyStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfRateHasChanged(2)) {
                    updateDatabase(2);
                    changeVisibility(2);
                    changeUserPoint(2);
                }
            }
        });

        thirdEmptyStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfRateHasChanged(3)) {
                    updateDatabase(3);
                    changeVisibility(3);
                    changeUserPoint(3);
                }
            }
        });

        firstFilledStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfRateHasChanged(1)) {
                    updateDatabase(1);
                    changeVisibility(1);
                    changeUserPoint(1);
                }
            }
        });

        secondFilledStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfRateHasChanged(2)) {
                    updateDatabase(2);
                    changeVisibility(2);
                    changeUserPoint(2);
                }
            }
        });

        thirdFilledStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfRateHasChanged(3)) {
                    updateDatabase(3);
                    changeVisibility(3);
                    changeUserPoint(3);
                }
            }
        });
    }

    private void changeUserPoint(int stars) {
        Log.d(TAG, "changeUserPoint: started");
        utils.increaseUserPoint(incomingItem, (stars - currentRate) * 2);
    }

    private void updateDatabase(int newRate) {
        Log.d(TAG, "updateDatabase: started");
        utils.updateTheRate(incomingItem, newRate);
    }

    private void changeVisibility(int newRate) {
        Log.d(TAG, "changeVisibility: started");
        switch (newRate) {
            case 1:
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.GONE);
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                break;
            case 2:
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                break;
            case 3:
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.GONE);
                break;
            case 0:
            default:
                firstFilledStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.GONE);
                firstEmptyStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean checkIfRateHasChanged(int newRate) {
        Log.d(TAG, "checkIfRateHasChanged: started");
        return newRate != currentRate;
    }

    private void initViews() {
        Log.d(TAG, "initViews: started");

        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDesc);
        txtAvailability = findViewById(R.id.txtAvailability);

        itemImage = findViewById(R.id.itemImage);

        btnAddToCart = findViewById(R.id.btnAddToCart);

        firstFilledStar = findViewById(R.id.firstFilledStar);
        secondFilledStar = findViewById(R.id.secondFilledStar);
        thirdFilledStar = findViewById(R.id.thirdFilledStar);
        firstEmptyStar = findViewById(R.id.firstEmptyStar);
        secondEmptyStar = findViewById(R.id.secondEmptyStar);
        thirdEmptyStar = findViewById(R.id.thirdEmptyStar);

        reviewsRecView = findViewById(R.id.reviewsRecView);

        addReviewRelLayout = findViewById(R.id.addReviewRelLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, TrackUserTime.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            unbindService(connection);
        }
    }
}
