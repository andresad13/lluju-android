package com.zheng.liuju.api;


import com.zheng.liuju.bean.AddCardList;
import com.zheng.liuju.bean.AgreementBean;
import com.zheng.liuju.bean.AmountList;
import com.zheng.liuju.bean.BorrowFinishBean;
import com.zheng.liuju.bean.BorrowFinishBeans;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.CheckBean;
import com.zheng.liuju.bean.ChongzhiDetailBean;
import com.zheng.liuju.bean.ChongzhiListBean;
import com.zheng.liuju.bean.CommonBean;
import com.zheng.liuju.bean.ExpensesBean;
import com.zheng.liuju.bean.ExpensesDatailBean;
import com.zheng.liuju.bean.FeedBackData;
import com.zheng.liuju.bean.Files;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.bean.Login;
import com.zheng.liuju.bean.LossToBuyBean;
import com.zheng.liuju.bean.OrderDetailsBean;
import com.zheng.liuju.bean.OrderListBean;
import com.zheng.liuju.bean.RechargeBean;
import com.zheng.liuju.bean.Register;
import com.zheng.liuju.bean.RulesBean;
import com.zheng.liuju.bean.ScanBean;
import com.zheng.liuju.bean.SendBean;
import com.zheng.liuju.bean.ServiceBean;
import com.zheng.liuju.bean.ShopBean;
import com.zheng.liuju.bean.ShopDetailsBean;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/UserApp/User/Info")
    Observable<Infos> info( @Header("token") String token,@Body RequestBody info);

    @POST("/UserApp/LoginAPI/sendsmg")
    Observable<SendBean> sendCode(@Body RequestBody info);

    @POST("/UserApp/LoginAPI/sendMailMsg")
    Observable<SendBean> sendEamilCode(@Body RequestBody info);

    @POST("/UserApp/LoginAPI/register")
    Observable<Register> register(@Body RequestBody info);


    @POST("/UserApp/LoginAPI/forget_pwd")
    Observable<Register> pwd(@Body RequestBody info);


    @POST("/UserApp/LoginAPI/login")
    Observable<Login> login(@Body RequestBody info);

    @POST("/UserApp/User/updInfo")
    Observable<Register> update(@Header("token") String token,@Body RequestBody info);


    @POST("/api/upload")
    @Multipart
    Observable<Files> uploadFiles(@Header("token") String token,@Part MultipartBody.Part file);

    @POST("/UserApp/More/cooperation/submit")
    Observable<Register> cooperation(@Header("token") String token,@Body RequestBody info);

    @POST("/UserApp/More/HelpList")
    Observable<CommonBean> problem(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/More/repair_reason")
    Observable<FeedBackData> feedback(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/More/repair")
    Observable<Register> feedbacks(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/More/about")
    Observable<ServiceBean> service(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/More/agreement")
    Observable<AgreementBean> agreement(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/More/privacy")
    Observable<AgreementBean> privacy(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/Shop/nearbyNoPager")
    Observable<ShopBean> mapShop(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/Shop/nearby2")
    Observable<ShopBean> shopList(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/Shop/details")
    Observable<ShopDetailsBean> details(@Query("shopId") String shopId,@Query("latitude") String latitude,@Query("longitude") String longitude, @Header("token") String token, @Body RequestBody info);

    @POST("/pay/mercadopago/getCards/{openId}")
    Observable<CardList> cardlist(@Header("token") String token, @Body RequestBody info,@Path ("openId") String openId);

    @POST("/UserApp/User/addCard/{token}")
    Observable<AddCardList> addcardlist(@Header("token") String token, @Body RequestBody info,@Path ("token") String tokens,@Query("email") String email
    ,@Query("code") String code,@Query("phone") String phone);

    @POST("/bank/delete/{id}")
    Observable<AddCardList> deletecardlist(@Path("id") String commentId, @Header("token") String token, @Body RequestBody info);


    @POST("/UserApp/User/rechargeInfo")
    Observable<AmountList> amountList(@Header("token") String token, @Body RequestBody info);

    @POST("/recharge/money")
    Observable<AddCardList> recharge(@Header("token") String token, @Body RequestBody info);

    @POST("/borrow/before/check")
    Observable<CheckBean> check(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/Borrow/borrow")
    Observable<ScanBean> scan(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/Borrow/Check")
    Observable<RulesBean> rules( @Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/Borrow/BorrowFinish")
    Observable<BorrowFinishBean> result(@Query("orderId") String commentId, @Header("token") String token, @Body RequestBody info,@Query("paymentId") String paymentId);
    @POST("/UserApp/User/recharge")
    Observable<RechargeBean> recharges(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/order/list")
    Observable<OrderListBean> orderlist(@Header("token") String token, @Body RequestBody info);

    @GET("/UserApp/order/detail")
    Observable<OrderDetailsBean> orderdetails(@Header("token") String token, @Query("orderId") String orderId );


    @POST("/UserApp/User/recharge/finish")
    Observable<BorrowFinishBeans> rechargefinish(@Query("id") String commentId, @Query("paymentId") String paymentId, @Header("token") String token, @Body RequestBody info);


    @POST("/UserApp/User/ChongzhiList")
    Observable<ChongzhiListBean> ChongzhiList(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/User/ChongzhiDetail")
    Observable<ChongzhiDetailBean> ChongzhiDetail(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/User/DropCard/{cardId}")
    Observable<Register> delete(@Path("cardId") String cardId,@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/User/expenses/list")
    Observable<ExpensesBean> expensesList(@Header("token") String token, @Body RequestBody info);


    @POST("/UserApp/Borrow/lossToBuy")
    Observable<LossToBuyBean> lossToBuy(@Query("orderId") String orderId, @Header("token") String token, @Body RequestBody info);


    @POST("/UserApp/User/expenses/detail")
    Observable<ExpensesDatailBean> expensesDatail(@Header("token") String token, @Body RequestBody info);

    @POST("/UserApp/User/refund")
    Observable<LossToBuyBean> refund(@Header("token") String token, @Body RequestBody info);

}
