package com.example.fakewedding.server;

import com.example.fakewedding.model.Album;
import com.example.fakewedding.model.ChangeAvatar;
import com.example.fakewedding.model.DetailAlbum;
import com.example.fakewedding.model.DetailUser;
import com.example.fakewedding.model.ImageUploadNam;
import com.example.fakewedding.model.ListCategory;
import com.example.fakewedding.model.ListTemple;
import com.example.fakewedding.model.ListTempleVideo;
import com.example.fakewedding.model.Login;
import com.example.fakewedding.model.Message;
import com.example.fakewedding.model.SwapEventData;
import com.example.fakewedding.model.TempleVideo;

import java.util.List;

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
    @FormUrlEncoded
    @POST(Server.CHANGE_AVATAR+"{page}")
    Call<ChangeAvatar> changeavatar(
            @Path("page") long id,
            @Header("Authorization") String authorization,
            @Field("link_img") String link_img,
            @Field("check_img") String check_img
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
    @GET(Server.DOMAIN2+"get/categories_wedding")
    Call<ListCategory> getCategory();
    @GET("https://databaseswap.mangasocial.online/get/list_image_wedding/1")
    Call<ListTemple> getListTemple(
             @Query("album") long albumId);


    @GET("https://thinkdiff.us/getdata/swap/listimage_wedding")
    Call<SwapEventData> swapImage(
            @Header("Authorization") String authorization,
            @Header("link1") String link1,
            @Header("link2") String link2,
            @Query("device_them_su_kien") String device_them_su_kien,
            @Query("ip_them_su_kien") String ip_them_su_kien,
            @Query("id_user") long id_user,
            @Query("list_folder") String list_folder
    );
    @GET("https://databaseswap.mangasocial.online/get/list_image/all_wedding_time")
    Call<List<Album>> listAlbum();
    @GET("https://databaseswap.mangasocial.online/get/list_2_image/id_image_swap_all_id_sk")
    Call<DetailAlbum> listDetailAlbum(
            @Query("id_user") int id_user,
            @Query("id_sk") String id_sk
    );
    @GET(Server.DOMAIN2+"get/list_2_image/id_image_swap")
    Call<List<Album>> listAlbumById(
            @Query("id_user") int id_user
    );
    @GET("https://databaseswap.mangasocial.online/get/list_video/all_video_wedding_template")
    Call<ListTempleVideo> getListVideo();
}




