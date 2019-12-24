package com.nexis.zeecinema

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nexis.zeecinema.FragmentFiles.HomeFragment
import com.nexis.zeecinema.FragmentFiles.TopRatedFragment
import com.nexis.zeecinema.FragmentFiles.UpComingFragment

class MainActivity : AppCompatActivity() {

    //--- Oluşturmuş Olduğum Fragmentleri ve BottomNavigationViewi Burada Late Initialize Ettim ---
    private lateinit var homeFragment : HomeFragment
    private lateinit var topRatedFragment : TopRatedFragment
    private lateinit var upComingFragment: UpComingFragment

    private lateinit var bottomBar : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //--- Fragmentleri Burada Tekrar Initialize Ettim ---
        homeFragment = HomeFragment()
        topRatedFragment = TopRatedFragment()
        upComingFragment = UpComingFragment()

        //--- bottomBarı Xml İle Bağladım ---
        bottomBar = findViewById(R.id.main_activity_bottomNavigationBar)

        //--- Başlangıç Olarak homeFragmenti Çağırdım ---
        setFragment(homeFragment)

        //--- bottomBara Tıklanırsa Olacakları Burada Belirttim ---
        bottomBar.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_menu_item_home -> {
                    setFragment(homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.bottom_menu_item_upcoming -> {
                    setFragment(upComingFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.bottom_menu_item_toprated -> {
                    setFragment(topRatedFragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }

            false
        }
    }

    //--- Fragmentleri Çağırmak İçin Oluşturmuş Olduğum Fonksiyon ---
    private fun setFragment(fragment : Fragment){
        val fragmentT : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentT.replace(R.id.main_activity_frameLayout, fragment)
        fragmentT.commit()
    }

    //--- Burada İse, Kullanıcı Geri Tuşuna Basınca Ekrana Bir Mesaj Gelmesini Sağladım ---
    override fun onBackPressed() {
        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle("Zee Cinema")
        alert.setIcon(R.mipmap.ic_zeecinema)
        alert.setMessage("Uygulamadan Çıkmak İstediğinize Emin Misiniz?")
        alert.setPositiveButton("Hayır"){dialog, which ->
            dialog.dismiss()
        }
        alert.setNegativeButton("Evet"){dialog, which ->
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        }

        alert.show()
    }
}
