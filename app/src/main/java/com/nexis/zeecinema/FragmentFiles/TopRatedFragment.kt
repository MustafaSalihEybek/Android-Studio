package com.nexis.zeecinema.FragmentFiles


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nexis.zeecinema.*
import com.nexis.zeecinema.AdapterFiles.MoviesAdapterTwo

/**
 * A simple [Fragment] subclass.
 */
class TopRatedFragment : Fragment() {

    //--- TopRated Datası İçin Gerekli Tanımlamarı Sonradan Initialize Edeceğimi, Ve Başlangıç Sayfa Numarasını Belirledim ---
    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapterTwo
    private lateinit var topRatedMoviesLayoutMgr: LinearLayoutManager
    private var topRatedMoviesPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_top_rated, container, false)

        //--- topRatedMoviesi Xmle Bağladım ---
        topRatedMovies = view.findViewById(R.id.fragment_topRated_recyclerView)

        //--- topRatedMovies İçin Gerekli Hizalamayı Yaptım ---
        topRatedMoviesLayoutMgr = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )

        //--- TopRated Movies Adapter Ayarı, Spacing Ayarı Ve OnClick ---
        topRatedMovies.layoutManager = topRatedMoviesLayoutMgr
        topRatedMoviesAdapter =
            MoviesAdapterTwo(mutableListOf())
        topRatedMovies.adapter = topRatedMoviesAdapter
        val decoration = UpComingFragment.VerticalSpaceItemDecoration(16)
        topRatedMovies.addItemDecoration(decoration)
        topRatedMoviesAdapter.setOnItemClickListener(object : MoviesAdapterTwo.OnItemClickListener{
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

        //--- İlk top_rated Datasını Aldım
        getMovies()

        return view
    }

    //--- top_rated Datasını Bir Öncekinin Üstüne Ekleyip Yeni Datayı Çağırdım ---
    private fun onTopRatedMoviesFetched(movies: List<Movie>){
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }

    private fun onError(){
        Toast.makeText(activity, "Lütfen Internet Bağlantınızı Kontrol Edin.!", Toast.LENGTH_SHORT).show()
    }

    //--- top_rated Datasını Sayfa Numarasına Göre Çağırıp, Adapter İçerisine Attım ---
    private fun getMovies(){
        MoviesRepository.getTopRatedMovies(
            topRatedMoviesPage,
            ::onTopRatedMoviesFetched,
            ::onError
        )
    }

    //--- top_rated Datasını Kullanıcı Her Aşağı Scroll Yaptığında Çağırıp Var Olan Önceki Datanın Üstüne Ekledim. ---
    private fun attachTopRatedMoviesOnScrollListener(){
        topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutMgr.itemCount
                val visibleItemCount = topRatedMoviesLayoutMgr.childCount
                val firstVisibleItem = topRatedMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2){
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
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
