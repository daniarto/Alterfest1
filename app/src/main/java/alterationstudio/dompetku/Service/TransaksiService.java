package alterationstudio.dompetku.Service;

import alterationstudio.dompetku.Response.GetTransaksi;
import alterationstudio.dompetku.Response.SetTransaksi;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by febrian on 12/6/16.
 */
public interface TransaksiService {
    @FormUrlEncoded
    @POST("v1/setTransaksi")
    Call<SetTransaksi> postTransaksi(@Field("user_key") String userKey,
                                     @Field("api_key") String apiKey,
                                     @Field("judul") String judul,
                                     @Field("tanggal") String tanggal,
                                     @Field("nominal") int nominal,
                                     @Field("keterangan") String keterangan,
                                     @Field("id_kategori") String kategoriKey);

    @FormUrlEncoded
    @POST("v1/getTransaksi")
    Call<GetTransaksi> postGetTransaksi(@Field("user_key") String userKey,@Field("api_key") String apiKey);

}
