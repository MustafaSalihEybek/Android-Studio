package com.nexis.zeecinema

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesRepository {
    //--- Api İçerisindeki Bilgimi Alıp MOVIES_API' İçerisine Attım ---
    private val MOVIES_API : MoviesApi

    //--- Initialize İşlemlerini Burada Gerçekleştirdim ---
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        MOVIES_API = retrofit.create(MoviesApi::class.java)
    }

    //--- upcoming Datası İçin Bir Fonksiyon Oluşturdum ---
    fun getUpcomingMovies(
        page : Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ){
        //--- Gelen Page Değerini Belirledim Page Değerine Eşitledim Ve Datayı Bir Callback İle Aldım ---
        MOVIES_API.getUpcomingMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse>{
                //--- onResponse Fonksiyonunu Override Ettim ---
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    //--- Datanın Gelme Durumunu Kontrol Ettim ---
                    if (response.isSuccessful){
                        //--- Data Alıp responseBody İçerisine Attım ---
                        val responseBody = response.body()

                        //--- Boş Değilse Kontrolü Koydum ---
                        if (responseBody != null)
                            //--- responseBody.moviesi onSuccess Fonksiyonu İle Çağırdım ---
                            onSuccess.invoke(responseBody.movies)
                        else
                            //--- Aksi Durumunda İse onError Fonksiyonunu Çağırdım ---
                            onError.invoke()
                    }else
                        onError.invoke()
                }

                //--- Bir Hata Olması Durumunda İse Override Edilmiş Fonksiyon Çalışır ---
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    Log.e("Repository", "onFailure", t)
                }
            })
    }

    //--- Burası da top_rated Datası İçin Oluşturmuş Olduğum Kod Bloğu ---
    fun getTopRatedMovies(
        page : Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ){
        MOVIES_API.getTopRatedMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null)
                            onSuccess.invoke(responseBody.movies)
                        else
                            onError.invoke()
                    }else
                        onError.invoke()
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    Log.e("Repository", "onFailure", t)
                }
            })
    }

    //--- Burası da popular Datası İçin Oluşturmuş Olduğum Kod Bloğu ---
    fun getPopularMovies(
        page : Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ){
        MOVIES_API.getPopularMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful){
                        val responseBody = response.body()

                        if (responseBody != null)
                            onSuccess.invoke(responseBody.movies)
                        else
                            onError.invoke()
                    }else
                        onError.invoke()
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    Log.e("Repository", "onFailure", t)
                }
            })
    }
}