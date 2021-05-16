package com.example.taller3appnoticias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taller3appnoticias.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticleAdapter
    private val articleList = mutableListOf<Article>()
    private var countr:String = ""
    private var categor:String = ""

    //variables de los botones
    lateinit var btnUsa: Button
    lateinit var btnRusia: Button
    lateinit var btnIndia: Button
    lateinit var btnColombia: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchNews.setOnQueryTextListener(this)

        btnUsa = findViewById(R.id.btnUsa)
        btnRusia = findViewById(R.id.btnRusia)
        btnIndia = findViewById(R.id.btnIndia)
        btnColombia = findViewById(R.id.btnColombia)

        initRecyclerView()
        searchNew("us","business")

        btnUsa.setOnClickListener {
            searchNew("us", "business")
        }
        btnRusia.setOnClickListener {
            searchNew("ru", "business")
        }

        btnIndia.setOnClickListener {
            searchNew("in", "business")
        }

        btnColombia.setOnClickListener {
            searchNew("co", "business")
        }

    }

    private fun initRecyclerView() {

        adapter = ArticleAdapter(articleList)
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.adapter = adapter
    }

    private fun searchNew(country: String, category: String):String {

        val api = Retrofit2()
        countr = country
        categor = category

        CoroutineScope(Dispatchers.IO).launch {

            val call = api.getService()?.getNewsByCategory(country, category,"73ae5fd097664473b48d5e61d626ffea")
            val news: NewsResponse? = call?.body()

            runOnUiThread{

                if(call!!.isSuccessful){
                    if(news?.status.equals("ok")) {

                    } else {
                        showMessage("Error en webservice!")

                    }
                    val article = news?.articles?:emptyList()
                    articleList.clear()
                    articleList.addAll(article)
                    adapter.notifyDataSetChanged()

                } else {
                    showMessage("Error en retrofit!")
                }
                hideKeyBoard()
            }

        }

        return country

    }

    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }


    private fun showMessage(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        var count: String = searchNew(countr, categor)
        if(!query.isNullOrEmpty()) {
            searchNew(count, query.toLowerCase(Locale.ROOT))
        }


        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}