package com.naitik.myndkdemo.Repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.naitik.myndkdemo.Classes.ClsCustomerOrderListParams;
import com.naitik.myndkdemo.Classes.ClsCustomerOrderResponse;
import com.naitik.myndkdemo.Classes.InterfaceGetOrderList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Repository {

    static {
        System.loadLibrary("native-lib");
    }

    public native String verifymobile();
    public native String getCustomer();

    private Context context;


    public Repository(Context context) {
        this.context = context;
    }

    public LiveData<ClsCustomerOrderResponse> getCustomerOrderList(ClsCustomerOrderListParams clsCustomerOrderListParams) {
        final MutableLiveData<ClsCustomerOrderResponse> mutableLiveData = new MutableLiveData<>();
        InterfaceGetOrderList myInterface = ApiClient.getRetrofitInstance().create(InterfaceGetOrderList.class);

        Call<ClsCustomerOrderResponse> call = myInterface.getCustomerOrderListAPI(getCustomer(),clsCustomerOrderListParams);
        Log.e("--URL--", "before call: " + call.request().url());

        call.enqueue(new Callback<ClsCustomerOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClsCustomerOrderResponse> call,
                                   @NonNull Response<ClsCustomerOrderResponse> response) {

                Gson gson = new Gson();
                String jsonInString = gson.toJson(response.body());
                Log.e("--List--", "orders: " + jsonInString);

                mutableLiveData.postValue(response.body());



            }

            @Override
            public void onFailure(Call<ClsCustomerOrderResponse> call, Throwable t) {


                Log.e("--List--", "t: " + t.getMessage());

                try {


                    mutableLiveData.postValue(null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return mutableLiveData;
    }


}
