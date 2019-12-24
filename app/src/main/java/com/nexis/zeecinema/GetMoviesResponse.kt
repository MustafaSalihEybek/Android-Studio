package com.nexis.zeecinema

import com.google.gson.annotations.SerializedName

//--- Constructur Oluşturup Movie Datasını Gelen Sayfa Numarasını Ve Toplamdaki Sayfa Sayısını Aldım ---
data class GetMoviesResponse(@SerializedName("page") val page: Int,
                             @SerializedName("results") val movies: List<Movie>,
                             @SerializedName("total_pages") val pages: Int) {
}