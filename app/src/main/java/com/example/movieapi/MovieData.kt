package com.example.movieapi
import com.google.gson.annotations.SerializedName


data class MovieData (

	@SerializedName("title") val title : String,
	@SerializedName("image") val image : String,
	@SerializedName("rating") val rating : Float,
	@SerializedName("releaseYear") val releaseYear : Int,
	@SerializedName("genre") val genre : List<String>
)