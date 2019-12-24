package com.nexis.zeecinema

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    //--- Kullanacağım Her Data İçin Ayrı @GET kullandım ---
    @GET("movie/popular")
    //--- ApiKey Ve Page Bilgilerini Burada Aldım ---
    fun getPopularMovies(
        @Query("api_key") apiKey : String = "a12b5fc37ef8953b30f96bb35bf65072",
        @Query("page") page : Int
    ) : Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey : String = "a12b5fc37ef8953b30f96bb35bf65072",
        @Query("page") page : Int
    ) : Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey : String = "a12b5fc37ef8953b30f96bb35bf65072",
        @Query("page") page : Int
    ) : Call<GetMoviesResponse>
}