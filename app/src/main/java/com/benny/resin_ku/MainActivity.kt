 package com.benny.resin_ku


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tentang.*


 class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_resin -> {
                this.supportActionBar?.title = "Resin"
                val fragment = Fragment_Resin.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_stamina -> {
                this.supportActionBar?.title = "Stamina"
                val fragment = Fragment_Stamina.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.title = "Resin"
        val navigation: BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navigation_resin -> {
                    supportActionBar?.title = "Resin"
                    navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.state_resin)
                    val fragment = Fragment_Resin.newInstance()
                    addFragment(fragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_stamina -> {
                    supportActionBar?.title = "Stamina"
                    navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.state_stamina)
                    val fragment = Fragment_Stamina.newInstance()
                    addFragment(fragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        navigation.itemIconTintList = null
        val fragment = Fragment_Resin.newInstance()
        addFragment(fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.help, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.ingfo -> {
                val dialog = BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.activity_tentang, null)
                dialog.setContentView(view)

                //inisiasi button tentang
                val btnTentang = view.findViewById<MaterialButton>(R.id.btnAbout)

                //inisiasi id
                val rbId = view.findViewById<RadioButton>(R.id.radioId)
                val tv1Id = view.findViewById<TextView>(R.id.tv1Id)
                val tv2Id = view.findViewById<TextView>(R.id.tv2Id)
                val tv3Id = view.findViewById<TextView>(R.id.tv3Id)
                val tv3PlusId = view.findViewById<TextView>(R.id.tv3PlusId)
                val tv4Id = view.findViewById<TextView>(R.id.tv4Id)
                val tv5Id = view.findViewById<TextView>(R.id.tv5Id)
                val tv6Id = view.findViewById<TextView>(R.id.tv6Id)
                val title = view.findViewById<TextView>(R.id.tvTitle)

                //inisiasi en
                val rbEn = view.findViewById<RadioButton>(R.id.radioEn)
                val tv1En = view.findViewById<TextView>(R.id.tv1En)
                val tv2En = view.findViewById<TextView>(R.id.tv2En)
                val tv3En = view.findViewById<TextView>(R.id.tv3En)
                val tv3PlusEn = view.findViewById<TextView>(R.id.tv3PlusEn)
                val tv4En = view.findViewById<TextView>(R.id.tv4En)
                val tv5En = view.findViewById<TextView>(R.id.tv5En)
                val tv6En = view.findViewById<TextView>(R.id.tv6En)

                rbId.setOnClickListener {
                    title.text = "Panduan penggunaan aplikasi"
                    tv1Id.visibility = View.VISIBLE
                    tv2Id.visibility = View.VISIBLE
                    tv3Id.visibility = View.VISIBLE
                    tv3PlusId.visibility = View.VISIBLE
                    tv4Id.visibility = View.VISIBLE
                    tv5Id.visibility = View.VISIBLE
                    tv6Id.visibility = View.VISIBLE

                    tv1En.visibility = View.GONE
                    tv2En.visibility = View.GONE
                    tv3En.visibility = View.GONE
                    tv3PlusEn.visibility = View.GONE
                    tv4En.visibility = View.GONE
                    tv5En.visibility = View.GONE
                    tv6En.visibility = View.GONE

                    btnTentang.text = "Tentang"
                }


                rbEn.setOnClickListener {

                    title.text = "Application usage guide"
                    tv1Id.visibility = View.GONE
                    tv2Id.visibility = View.GONE
                    tv3Id.visibility = View.GONE
                    tv3PlusId.visibility = View.GONE
                    tv4Id.visibility = View.GONE
                    tv5Id.visibility = View.GONE
                    tv6Id.visibility = View.GONE

                    tv1En.visibility = View.VISIBLE
                    tv2En.visibility = View.VISIBLE
                    tv3En.visibility = View.VISIBLE
                    tv3PlusEn.visibility = View.VISIBLE
                    tv4En.visibility = View.VISIBLE
                    tv5En.visibility = View.VISIBLE
                    tv6En.visibility = View.VISIBLE

                    btnTentang.text = "About"
                }

                btnTentang.setOnClickListener {
                    startActivity(Intent(applicationContext, ActivityInfo::class.java))
                }

                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}