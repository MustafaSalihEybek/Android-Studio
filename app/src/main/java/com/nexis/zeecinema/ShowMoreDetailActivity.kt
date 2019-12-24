package com.nexis.zeecinema

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class ShowMoreDetailActivity : AppCompatActivity() {

    //--- ImageView Ve TextViewleri Burada Late Initialize Ettim ---
    private lateinit var moviePoster : ImageView
    private lateinit var movieName : TextView
    private lateinit var movieReleaseDate : TextView
    private lateinit var movieImdb : TextView
    private lateinit var movieSummary : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_more_detail)

        //--- Initialize Ettiğim ImageView Ve TextViewleri Xml İle Bağladım ---
        moviePoster = findViewById(R.id.show_more_detail_activity_imageViewBackDrop)
        movieName = findViewById(R.id.show_more_detail_activity_textViewMovieName)
        movieReleaseDate = findViewById(R.id.show_more_detail_activity_textViewMovieReleaseDate)
        movieImdb = findViewById(R.id.show_more_detail_activity_textViewMovieImdb)
        movieSummary = findViewById(R.id.show_more_detail_activity_textViewMovieSummary)

        //--- Gelen Intenti Aldım ---
        val intent = intent

        //--- Gelen Film İsmini Yeni Bar İsmi Olarak Değiştirdim ---
        val actionBar = supportActionBar
        actionBar!!.title = intent.getStringExtra("movieName")

        //--- Gelen Poster Yolunu movieBackDrop İle Tanımlamış Olduğum ImageView İçerisine Attım ---
        Glide.with(this)
            .load(intent.getStringExtra("moviePoster"))
            .transform(CenterCrop())
            .into(moviePoster)

        //--- Geriye Kalan Dataları İse TextViewler İçerisine Sırasıyla Attım ---
        movieName.text = intent.getStringExtra("movieName")
        movieReleaseDate.text = intent.getStringExtra("movieReleaseDate")
        movieImdb.text = intent.getStringExtra("movieImdb")
        movieSummary.text = intent.getStringExtra("movieSummary")
    }
}
