package com.espresso.data

import com.espresso.data.models.company.CompanyModel
import com.espresso.data.models.history.RefuelHistoryModel
import com.espresso.data.models.pay.PayWithInvoiceModel
import com.espresso.data.models.pay.PayWithReceiptModel
import com.espresso.data.models.profile.UserProfile
import com.espresso.data.models.profile.UserRegisterInfo
import com.espresso.data.models.refuel.RefuelProduct
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BackendService {
    //USER
    @GET("/user/{id}")
    fun getUser(@Path("id") id: Long): Single<UserProfile>

    @GET("/user/profile/{id}")
    fun profile(@Path("id") id: Long): Single<UserProfile>

    @POST("/user/register")
    fun register(@Body userInfo: UserRegisterInfo): Single<UserProfile>

    //PRODUCTS
    @GET("/product/getAll")
    fun getRefuelProducts(): Single<List<RefuelProduct>>

    @POST("receipt/saveReceipt")
    fun payWithReceipt(@Body receiptInfo: PayWithReceiptModel): Completable

    @GET("refuelingHistory/getAllByUser/{id}")
    fun getRefuelHistory(@Path("id") id: Long): Single<List<RefuelHistoryModel>>

    @POST("invoice/saveInvoice")
    fun payWithInvoice(@Body invoiceInfo: PayWithInvoiceModel): Completable

    @POST("/company/addCompany")
    fun addUserCompany(@Body companyModel: CompanyModel): Completable

    @GET("/company/getCompanyByUser/{id}")
    fun getUserCompany(@Path("id") userId: Long): Single<CompanyModel>

}
