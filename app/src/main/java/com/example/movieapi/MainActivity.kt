package com.example.movieapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.*
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit



class MainActivity : AppCompatActivity(), DataAdapter.Listener  {

    private val BASE_URL = "https://api.androidhive.info/"
    private var mCompositeDisposable: CompositeDisposable? = null
    private var movieArrayList: ArrayList<MovieData>? = null
    lateinit var ratingBar:RatingBar
    lateinit var genera:TextView
    lateinit var releaseYear:TextView
    lateinit var movieName:TextView
    lateinit var movieImage:ImageView
    var myMovies:MutableList<String> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mCompositeDisposable = CompositeDisposable()
        viewManager = LinearLayoutManager(this)
        viewAdapter = DataAdapter(myMovies as ArrayList<String>, this)

        recyclerView = findViewById<RecyclerView>(R.id.myRecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }



        loadJSON()

    }

    private fun loadJSON() {

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieApiService::class.java)

        mCompositeDisposable?.add(requestInterface.getMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError))

    }


    private fun handleResponse(androidList: List<MovieData>) {
      // Toast.makeText(this, "Test1", Toast.LENGTH_SHORT).show()
        myMovies.clear()
        movieArrayList = ArrayList(androidList)
        try {
            for (x in 0 until movieArrayList!!.count()){
                myMovies.add(movieArrayList!![x].title)
            }
                (recyclerView.adapter as DataAdapter).notifyDataSetChanged()

        }catch (e:Exception){

        }


    }



    private fun handleError(error: Throwable) {


        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    private fun initilizeDialog(myPosition: Int) {


        val mDialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.movie_details, null)

        movieName = mDialogView.findViewById(R.id.movieName)
        ratingBar =mDialogView.findViewById(R.id.movieRating)
        releaseYear=mDialogView.findViewById(R.id.release_Year)
        genera =mDialogView.findViewById(R.id.genera)
        movieImage=mDialogView.findViewById(R.id.movieImage)
        var dismissButton: Button = mDialogView.findViewById(R.id.dismissBtn)



        try {
            var myGeneras:String = movieArrayList!![myPosition].genre[0]
            for (x in 1 until movieArrayList!![myPosition].genre.size){
                myGeneras = myGeneras + "' "+movieArrayList!![myPosition].genre[x]
            }
            movieName.text = movieArrayList?.get(myPosition)?.title
            releaseYear.text = "Release Year: "+movieArrayList?.get(myPosition)?.releaseYear.toString()
            ratingBar.rating = (movieArrayList?.get(myPosition)?.rating!!)/2
            genera.text = "Genre: " + myGeneras
            Picasso.get().load(movieArrayList?.get(myPosition)?.image).into(movieImage);
        }catch (e:Exception){

        }

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val  mAlertDialog = mBuilder.show()
        dismissButton.setOnClickListener{
            mAlertDialog.dismiss()
        }
    }

    override fun onItemClick(position: Int) {
        initilizeDialog(position)
    }


}
