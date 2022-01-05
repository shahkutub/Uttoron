package gov.bd.mpportal.utils

import com.uttoron.model.AllDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiKt {


//    holiday_reason_id:1
//    from_date:2021-07-02
//    to_date:2021-07-04
//    note:sdffsdfd

//    @Multipart
//    @POST("api/mp-leave/leave-application")
//    fun updateProfileWithPhoto(
//        //@Header("Authorization") String authorization,
//        @Part image: MultipartBody.Part?,
//        @Part("token") token: RequestBody?,
//        @Part("holiday_reason_id") id: RequestBody?,
//        @Part("note") note: RequestBody?,
//        @Part("from_date") from_date: RequestBody?,
//        @Part("to_date") to_date: RequestBody?
//
//        ): Call<FileUploadResponse>

//    @Multipart
//    @POST("api/app-management/digital-support-question/store")
//    fun digitalSupportSubmit(
//        @Header("Authorization") authorization: String,
//        @Part image: MultipartBody.Part?,
//        @Part("_token") token: RequestBody?,
//        @Part("question") question: RequestBody?
//    ): Call<SubmitRespne>

        @GET("api/uttoronapi")
    fun getAllData(
    ): Call<AllDataResponse>



}