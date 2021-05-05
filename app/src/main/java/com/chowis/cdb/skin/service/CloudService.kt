package com.chowis.cdb.skin.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

@JvmSuppressWildcards
interface CloudService {
    @Multipart
    @POST("/upload/CNDP123456789")
    fun uploadImage(
            @HeaderMap headers: Map<String, Any>,
            @Part fileUpload: MultipartBody.Part,
            @Part("device") device: RequestBody,
            @Part("bm_id") bmId: RequestBody,
            @Part("brand") brand: RequestBody,
            @Part("program_before") programBefore: RequestBody,
            @Part("program_after") programAfter: RequestBody
    ): Call<ResponseBody>
}