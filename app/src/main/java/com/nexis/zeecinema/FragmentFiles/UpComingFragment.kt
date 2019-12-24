package com.nexis.zeecinema.FragmentFiles


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nexis.zeecinema.*
import com.nexis.zeecinema.AdapterFiles.MoviesAdapterTwo

/**
 * A simple [Fragment] subclass.
 */
class UpComingFragment : Fragment() {

    //--- UpComing Datası İçin Gerekli Tanımlamarı Sonradan Initialize Edeceğimi, Ve Başlangıç Sayfa Numarasını Belirledim ---
    lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MoviesAdapterTwo
    private lateinit var upcomingMoviesLayoutMng: LinearLayoutManager
    private var upcomingMoviesPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //--- fragment_up_coming Layoutu Class İle Bağlayıp viewe Eşitledim Ve Geriye viewi Dönderdim ---
        val view: View = inflater.inflate(R.layout.fragment_up_coming, container, false)

        //--- upcomingMoviesi Xmle Bağladım ---
        upcomingMovies = view.findViewById(R.id.fragment_upComing_recyclerView)

        //--- upcomingMovies İçin Gerekli Hizalamayı Yaptım ---
        upcomingMoviesLayoutMng = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )

        //--- UpComing Movies Adapter Ayarı, Spacing Ayarı Ve OnClick ---
        upcomingMovies.layoutManager = upcomingMoviesLayoutMng
        upcomingMoviesAdapter =
            MoviesAdapterTwo(mutableListOf())
        upcomingMovies.adapter = upcomingMoviesAdapter
        val decoration = VerticalSpaceItemDecoration(16)
        upcomingMovies.addItemDecoration(decoration)
        upcomingMoviesAdapter.setOnItemClickListener(object : MoviesAdapterTwo.OnItemClickListener {
            override fun onItemClick(
                moviePoster: String,
                movieName: String,
                movieReleaseDate: String,
                movieImdb: String,
                movieSummary: String
            ) {
                val intent = Intent(activity, ShowMoreDetailActivity::class.java)

                intent.putExtra("moviePoster", moviePoster)
                intent.putExtra("movieName", movieName)
                intent.putExtra("movieReleaseDate", movieReleaseDate)
                intent.putExtra("movieImdb", movieImdb)
                intent.putExtra("movieSummary", movieSummary)

                startActivity(intent)
            }
        })

        //--- İlk upcoming Datasını Aldım
        getMovies()

        return view
    }

    //--- upcoming Datasını Bir Öncekinin Üstüne Ekleyip Yeni Datayı Çağırdım ---
    private fun onUpComingMoviesFetched(movies: List<Movie>) {
        upcomingMoviesAdapter.appendMovies(movies)
        attachUpcomingMoviesOnScrollListener()
    }

    private fun onError() {
        Toast.makeText(activity, "Lütfen Internet Bağlantınızı Kontrol Edin.!", Toast.LENGTH_SHORT)
            .show()
    }

    //--- upcoming Datasını Sayfa Numarasına Göre Çağırıp, Adapter İçerisine Attım ---
    private fun getMovies() {
        MoviesRepository.getUpcomingMovies(
            upcomingMoviesPage,
            ::onUpComingMoviesFetched,
            ::onError
        )
    }

    //--- upcoming Datasını Kullanıcı Her Aşağı Scroll Yaptığında Çağırıp Var Olan Önceki Datanın Üstüne Ekledim. ---
    private fun attachUpcomingMoviesOnScrollListener() {
        upcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutMng.itemCount
                val visibleItemCount = upcomingMoviesLayoutMng.childCount
                val firstVisibleItem = upcomingMoviesLayoutMng.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    upcomingMovies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getMovies()
                }
            }
        })
    }

    class VerticalSpaceItemDecoration(var verticalSpaceHeight : Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter()!!.getItemCount() - 1) {
                outRect.bottom = verticalSpaceHeight
            }
        }
    }
}
