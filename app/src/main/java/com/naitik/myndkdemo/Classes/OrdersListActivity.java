package com.naitik.myndkdemo.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naitik.myndkdemo.R;

import java.util.ArrayList;
import java.util.List;


public class OrdersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderListViewModel orderListViewModel;
    private ProgressBar progress_bar;
    private ProgressDialog pd;
    private List<ClsCustomerOrderResponseList> responseOrderList = new ArrayList<>();
    private AdapterCustomerOrderList adapterOrderList;
    RelativeLayout ll_no_order_found;
    TextView tv_new_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        main();
    }

    public ProgressDialog _prProgressDialog(Context c, String msg, Boolean setCancelable) {
        ProgressDialog progressDialog = new ProgressDialog(c, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(msg); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(setCancelable);
        return progressDialog;
    }

    private void main() {

        orderListViewModel = new ViewModelProvider(OrdersListActivity.this).get(OrderListViewModel.class);

        tv_new_order = findViewById(R.id.tv_new_order);
        ll_no_order_found = findViewById(R.id.ll_no_order_found);
        recyclerView = findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        progress_bar = findViewById(R.id.progress_bar);

        adapterOrderList = new AdapterCustomerOrderList(getApplicationContext());
        recyclerView.setAdapter(adapterOrderList);


        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                progress_bar.setVisibility(View.VISIBLE);

                if (CheckInternetConnection(getApplicationContext())) {
                    Log.d("--per--", "Not Granted: ");
                    getOrderListAPI(++page);
                } else {
                    Log.d("--per--", "Already Granted: ");
                    progress_bar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(),
                            "Please check your internet connection!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        pd = _prProgressDialog(OrdersListActivity.this, "Getting list...", false);
        pd.show();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (CheckInternetConnection(OrdersListActivity.this)) {
            responseOrderList.clear();
            adapterOrderList.notifyDataSetChanged();
            getOrderListAPI(1);
        } else {
            pd.dismiss();
            Toast.makeText(getApplicationContext(),
                    "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    void getOrderListAPI(int CurrentIndex) {
        ClsCustomerOrderListParams obj = new ClsCustomerOrderListParams();
        obj.setCurrentIndex(CurrentIndex);
        obj.setMobileNo("8469999535");
        obj.setCustomerID(Integer.parseInt("1"));

        orderListViewModel.getCustomerOrderList(obj).observe(this,
                clsCustomerOrderResponse -> {

                    if (clsCustomerOrderResponse != null) {
                        pd.dismiss();

                        String msg = clsCustomerOrderResponse.getSuccess();
                        int _totalPages = clsCustomerOrderResponse.getTotalPages();

                        if ("0".equals(msg)) {
                            if (responseOrderList.size() <= 0) {
                                ll_no_order_found.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            progress_bar.setVisibility(View.GONE);

                        } else if ("1".equals(msg)) {
                            if (clsCustomerOrderResponse.getData().size() > 0) {
                                responseOrderList.addAll(clsCustomerOrderResponse.getData());
                            }
                            if (responseOrderList.size() > 0) {
                                ll_no_order_found.setVisibility(View.GONE);
                                progress_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                adapterOrderList.addList(responseOrderList);
                            }
                        }

                    }
                });
    }

    public static boolean CheckInternetConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}