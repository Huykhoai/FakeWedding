package com.example.fakewedding.server;

import com.example.fakewedding.model.DetailUser;
import com.example.fakewedding.model.ImageUploadNam;
import com.example.fakewedding.model.Login;
import com.example.fakewedding.model.Message;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @Multipart
    @POST(Server.URI_UPLOAD_IMAGE+ "{page}")
    Call<String> uploadImage(
            @Path("page") int id_user,
            @Query("type") String fileType,
            @Part MultipartBody.Part src_img
            );
    @FormUrlEncoded
    @POST(Server.URI_FORGOT_PASS)
    Call<Message> sendData(
            @Field("email") String email
    );
    @GET(Server.GET_PROFILE+"{page}")
    Call<DetailUser> getUSer(
      @Path("page") long id
    );
    @FormUrlEncoded
    @POST(Server.CHANGE_PASSWORD+"{page}")
    Call<Object> changepass(
            @Path("page") long id,
            @Header("Authorization") String authorization,
            @Field("old_password") String oldpassword,
            @Field("new_password") String newpassword
    );
    @GET(Server.GET_UPLOAD_IMG+"{page}")
    Call<ImageUploadNam> getImageNam(
            @Path("page") long id,
            @Query("type") String type
    );

}

