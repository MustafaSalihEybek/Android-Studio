package com.nexis.zeecinema.FragmentFiles


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nexis.zeecinema.Movie
import com.nexis.zeecinema.AdapterFiles.MoviesAdapter
import com.nexis.zeecinema.MoviesRepository
import com.nexis.zeecinema.R
import com.nexis.zeecinema.ShowMoreDetailActivity

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    //--- Popular Datası İçin Gerekli Tanımlamarı Sonradan Initialize Edeceğimi, Ve Başlangıç Sayfa Numarasını Belirledim ---
    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    private var popularMoviesPage = 1

    //--- TopRated Datası İçin Gerekli Tanımlamarı Sonradan Initialize Edeceğimi, Ve Başlangıç Sayfa Numarasını Belirledim ---
    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var topRatedMoviesLayoutMgr: LinearLayoutManager
    private var topRatedMoviesPage = 1

    //--- UpComing Datası İçin Gerekli Tanımlamarı Sonradan Initialize Edeceğimi, Ve Başlangıç Sayfa Numarasını Belirledim ---
    private lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MoviesAdapter
    private lateinit var upcomingMoviesLayoutMng: LinearLayoutManager
    private var upcomingMoviesPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)

        //--- RecyclerViewleri Xml Kısmına Bağladım ---
        popularMovies = view.findViewById(R.id.fragment_home_popularMoviesRecyclerView)
        topRatedMovies = view.findViewById(R.id.fragment_home_topRatedMoviesRecyclerView)
        upcomingMovies = view.findViewById(R.id.fragment_home_upcomingMoviesRecyclerView)

        //--- İlgili RecyclerViewler İçin Gerekli Hizalamayı Yaptım ---
        popularMoviesLayoutMgr = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedMoviesLayoutMgr = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        upcomingMoviesLayoutMng = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        //--- Popular Movies Adapter Ayarı Ve OnClick ---
        popularMovies.layoutManager = popularMoviesLayoutMgr
        popularMoviesAdapter =
            MoviesAdapter(mutableListOf())
        popularMovies.adapter = popularMoviesAdapter
        popularMoviesAdapter.setOnItemClickListener(object : MoviesAdapter.OnItemClickListener{
            override fun onItemClick(
                moviePoster: String,
                movieName: String,
                movieReleaseDate: String,
                movieImdb: String,
                movieSummary: String
            ) {
                startIntent(moviePoster, movieName, movieReleaseDate, movieImdb, movieSummary)
            }
        })

        //--- TopRated Movies Adapter Ayarı Ve OnClick ---
        topRatedMovies.layoutManager = topRatedMoviesLayoutMgr
        topRatedMoviesAdapter =
            MoviesAdapter(mutableListOf())
        topRatedMovies.adapter = topRatedMoviesAdapter
        topRatedMoviesAdapter.setOnItemClickListener(object : MoviesAdapter.OnItemClickListener{
            override fun onItemClick(
                moviePoster: String,
                movieName: String,
                movieReleaseDate: String,
                movieImdb: String,
                movieSummary: String
            ) {
                startIntent(moviePoster, movieName, movieReleaseDate, movieImdb, movieSummary)
            }
        })

        //--- UpComing Movies Adapter Ayarı Ve OnClick ---
        upcomingMovies.layoutManager = upcomingMoviesLayoutMng
        upcomingMoviesAdapter =
            MoviesAdapter(mutableListOf())
        upcomingMovies.adapter = upcomingMoviesAdapter
        upcomingMoviesAdapter.setOnItemClickListener(object : MoviesAdapter.OnItemClickListener{
            override fun onItemClick(
                moviePoster: String,
                movieName: String,
                movieReleaseDate: String,
                movieImdb: String,
                movieSummary: String
            ) {
                startIntent(moviePoster, movieName, movieReleaseDate, movieImdb, movieSummary)
            }
        })

        //--- Dataların İlk Sayfasını Aldım ---
        getMovies("popular")
        getMovies("top_rated")
        getMovies("upcoming")

        return view
    }

    //--- 3 Farklı Yerde Yeni Aktivite Başlatmam Gerekiyordu Bu Yüzden Fonksiyon İçerisine Aldım ---
    private fun startIntent(moviePoster: String, movieName: String, movieReleaseDate: String, movieImdb: String, movieSummary: String) {
        val intent = Intent(activity, ShowMoreDetailActivity::class.java)

        intent.putExtra("moviePoster", moviePoster)
        intent.putExtra("movieName", movieName)
        intent.putExtra("movieReleaseDate", movieReleaseDate)
        intent.putExtra("movieImdb", movieImdb)
        intent.putExtra("movieSummary", movieSummary)

        startActivity(intent)
    }

    //--- UpComing Datasını Bir Öncekinin Üstüne Ekleyip Yeni Datayı Çağırdım ---
    private fun onUpComingMoviesFetched(movies: List<Movie>){
        upcomingMoviesAdapter.appendMovies(movies)
        attachUpcomingMoviesOnScrollListener()
    }

    //--- Popular Datasını Bir Öncekinin Üstüne Ekleyip Yeni Datayı Çağırdım ---
    private fun onPopularMoviesFetched(movies: List<Movie>){
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }

    //--- TopRated Datasını Bir Öncekinin Üstüne Ekleyip Yeni Datayı Çağırdım ---
    private fun onTopRatedMoviesFetched(movies: List<Movie>){
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }

    private fun onError(){
        Toast.makeText(activity, "Lütfen Internet Bağlantınızı Kontrol Edin.!", Toast.LENGTH_SHORT).show()
    }

    // Dataları İlgili Sayfa Numarasına Göre Çağırdığım Kısım 3 Farklı Data Olduğu İçin Fonksiyon İçine Aldım ---
    private fun getMovies(movie : String){
        if (movie.equals("popular")){
            MoviesRepository.getPopularMovies(
                popularMoviesPage,
                ::onPopularMoviesFetched,
                ::onError
            )
        }else if (movie.equals("top_rated")){
            MoviesRepository.getTopRatedMovies(
                topRatedMoviesPage,
                ::onTopRatedMoviesFetched,
                ::onError
            )
        }else {
            MoviesRepository.getUpcomingMovies(
                upcomingMoviesPage,
                ::onUpComingMoviesFetched,
                ::onError
            )
        }
    }

    //--- Upcoming Datasını Kullanıcı Her Aşağı Scroll Yaptığında Çağırıp Var Olan Önceki Datanın Üstüne Ekledim. ---
    private fun attachUpcomingMoviesOnScrollListener(){
        upcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutMng.itemCount
                val visibleItemCount = upcomingMoviesLayoutMng.childCount
                val firstVisibleItem = upcomingMoviesLayoutMng.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2){
                    upcomingMovies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getMovies("upcoming")
                }
            }
        })
    }

    //--- TopRated Datasını Kullanıcı Her Aşağı Scroll Yaptığında Çağırıp Var Olan Önceki Datanın Üstüne Ekledim. ---
    private fun attachTopRatedMoviesOnScrollListener(){
        topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutMgr.itemCount
                val visibleItemCount = topRatedMoviesLayoutMgr.childCount
                val firstVisibleItem = topRatedMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2){
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getMovies("top_rated")
                }
            }
        })
    }

    //--- Popular Datasını Kullanıcı Her Aşağı Scroll Yaptığında Çağırıp Var Olan Önceki Datanın Üstüne Ekledim. ---
    private fun attachPopularMoviesOnScrollListener(){
        popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2){
                    popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getMovies("popular")
                }
            }
        })
    }
}
