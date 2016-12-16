package alterationstudio.dompetku.Service;

import alterationstudio.dompetku.Response.Login;
import alterationstudio.dompetku.Response.Register;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by febrian on 12/6/16.
 */
public interface LoginService {

    @FormUrlEncoded
    @POST("v1/register")
    Call<Register> postRegister(@Field("email") String email,
                                @Field("username") String username,
                                @Field("password") String password,
                                @Field("api_key") String apiKey);

    @FormUrlEncoded
    @POST("v1/login")
    Call<Login> postLogin(@Field("email") String email,
                          @Field("password") String password,
                          @Field("api_key") String apiKey);
}
