package com.nexis.zeecinema.AdapterFiles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.nexis.zeecinema.Movie
import com.nexis.zeecinema.R

class MoviesAdapter(
    //--- Constructur oluşturup Movie Türünde MutableList Tanımladım Ve Onu moviese Eşitledim ---
    private var movies : MutableList<Movie>
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(){

    //--- listeneri Oluşturmuş Olduğum Interface Türünde Late Initialze Ettim ---
    private lateinit var listener : OnItemClickListener

    //--- ShowMoreDetailActivity İçerisinde Film İle İlgili Göstermek İstediğim Datalar İçin Listler Tanımladım ---
    private var arrayListMovieName = ArrayList<String>()
    private var arrayListMovieReleaseDate = ArrayList<String>()
    private var arrayListMovieImdb = ArrayList<String>()
    private var arrayListMovieSummary = ArrayList<String>()
    private val arrayListMoviePoster = ArrayList<String>()

    //--- item_movie Layoutunu parente Bağladım Ve Geriye Layoutu Bir View Olarak Döndürdürm ---
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    //--- movie İçerisindeki Eleman Sayısını Aldım ---
    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        //--- moviesin position Indeksindeki Değeri Bağladım ---
        holder.bind(movies[position])

        //--- Onclick Olayını İse Burada Yaptım (Tıklanan Layout İndeksine Göre İşlem Yapar) ---
        holder.itemView.setOnClickListener {
            val pos = position
            if (pos != RecyclerView.NO_POSITION){
                listener.onItemClick(arrayListMoviePoster[pos],
                                     arrayListMovieName[pos],
                                     arrayListMovieReleaseDate[pos],
                                     arrayListMovieImdb[pos],
                                     arrayListMovieSummary[pos])
            }
        }
    }

    //--- Gelen Film Datalarını Alıp Var Olan Adapterin Üstüne Ekledim ---
    fun appendMovies(movies : List<Movie>){
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    //--- ViewHolder Türünde MovieViewHolder Classını Oluşturdum ---
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //--- Layout İçerisindekilerini Bir View Yardımı İle Bağladım ---
        private val poster : ImageView = itemView.findViewById(R.id.item_movie_poster)
        private val imdb : TextView = itemView.findViewById(R.id.item_movie_imdb)

        //--- movie İçerisindeki Datayı Layout İçerisindeki Nesnelerime Ve Listlere Attım ---
        fun bind(movie: Movie){
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${movie.backdropPath}")
                .transform(CenterCrop())
                .into(poster)

            imdb.text = "Imdb: ${movie.rating}"

            arrayListMoviePoster.add("https://image.tmdb.org/t/p/w342${movie.posterPath}")
            arrayListMovieName.add(movie.title)
            arrayListMovieReleaseDate.add(movie.releaseDate)
            arrayListMovieImdb.add(movie.rating.toString())
            arrayListMovieSummary.add(movie.overview)
        }
    }

    //--- OnItemClickListener Interfaci Oluşturdum ---
    interface OnItemClickListener{
        // İçerisine onItemClick Fonksiyonu Tanımladım RecyclerView İçerisindeki Elemana Tıklanınca Bu Veriler İşlenecek ---
        fun onItemClick(moviePoster : String,
                        movieName : String,
                        movieReleaseDate : String,
                        movieImdb : String,
                        movieSummary : String)
    }

    // setOnItemClickListener Fonksiyonu Oluşturdum Parametre Olarak İse, OnItemClickListener Interfacini Çağırıp listenera Eşitledim ---
    fun setOnItemClickListener(listener : OnItemClickListener){
        //--- listenerleri Birbirine Eşitledim ---
        this.listener = listener
    }
}