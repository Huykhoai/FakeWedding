package com.example.fakewedding.server;

import com.example.fakewedding.model.DetailEventListParent;
import com.example.fakewedding.model.Login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServer {
    @FormUrlEncoded
    @POST(Server.URI_LOG_IN)
    Call<Login> login(
            @Field("email_or_username") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST(Server.URI_SIGN_UP)
    Call<Object> signup(
            @Field("email") String email,
            @Field("password") String password,
            @Field("user_name") String userName,
            @Field("link_avatar") String linkAvatar,
            @Field("ip_register") String registerIp
    );
    @GET(Server.URI_LIST_EVENT_HOME+ "{page}")
    Call<DetailEventListParent> getEventListforHome(
            @Path("page") long id,
            @Query("id_user")int id_user);
}

