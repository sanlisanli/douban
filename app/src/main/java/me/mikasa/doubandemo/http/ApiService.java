package me.mikasa.doubandemo.http;

import me.mikasa.doubandemo.bean.Movie;
import me.mikasa.doubandemo.bean.Theater;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @GET("in_theaters")
    Observable<Theater>getTheaterMovie(@Query("start")int start, @Query("count")int count);

    @GET("top250")
    Observable<Movie>getMovie(@Query("start")int start,@Query("count")int count);
    //query要传入的参数？？
}
