package com.naitik.myndkdemo.Classes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface InterfaceGetOrderList {

//    @POST("CustomerV1/GetCustomerOrderList")
//    Call<ClsCustomerOrderResponse> getCustomerOrderListAPI(@Body ClsCustomerOrderListParams obj);


    @POST
    Call<ClsCustomerOrderResponse> getCustomerOrderListAPI(@Url String url,
                                                           @Body ClsCustomerOrderListParams obj);


//    https://stackoverflow.com/questions/32559333/retrofit-2-dynamic-url


}
