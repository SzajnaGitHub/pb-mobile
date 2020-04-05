package com.espresso.data

import com.espresso.data.models.company.CompanyModel
import com.espresso.data.models.company.InfoModel
import com.espresso.data.models.history.RefuelHistoryModel
import com.espresso.data.models.pay.PayWithInvoiceModel
import com.espresso.data.models.pay.PayWithReceiptModel
import com.espresso.data.models.profile.UserProfile
import com.espresso.data.models.profile.UserRegisterInfo
import com.espresso.data.models.refuel.RefuelProduct
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface BackendService {
    //USER
    @GET("/user/{id}")
    fun getUser(@Path("id") id: Long): Single<UserProfile>

    @GET("/user/profile/{id}")
    fun profile(@Path("id") id: Long): Single<UserProfile>

    @POST("/user/register")
    fun register(@Body userInfo: UserRegisterInfo): Single<UserProfile>

    //REFUEL
    @GET("/product/getAll")
    fun getRefuelProducts(): Single<List<RefuelProduct>>

    @GET("refuelingHistory/getAllByUser/{id}")
    fun getRefuelHistory(@Path("id") id: Long): Single<List<RefuelHistoryModel>>

    //PAY
    @POST("receipt/saveReceipt")
    fun payWithReceipt(@Body receiptInfo: PayWithReceiptModel): Completable

    @POST("invoice/saveInvoice")
    fun payWithInvoice(@Body invoiceInfo: PayWithInvoiceModel): Completable

    //COMPANY
    @POST("/company/addCompany/{id}")
    fun addUserCompany(
        @Path("id") id: Long,
        @Body companyModel: CompanyModel
    ): Completable

    @GET("/company/getCompanyByUser/{id}")
    fun getUserCompany(@Path("id") userId: Long): Single<CompanyModel>

    //APP INFO

    @GET("/infoApp/getInfo")
    fun getCompanyInfo(): Single<InfoModel>


    //LOYALTY PRODUCTS
    @PUT("/loyaltyProduct/changePointsToLoyaltyProduct/{id}/{productId}")
    fun getReward(
        @Path("id") id: Long,
        @Path("productId") productId: Long
    ): Completable
}
