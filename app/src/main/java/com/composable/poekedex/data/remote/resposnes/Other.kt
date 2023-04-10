package com.composable.poekedex.data.remote.resposnes


import com.google.gson.annotations.SerializedName

data class Other(
    @SerializedName("dream_world")
    val dreamWorld: DreamWorld,
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)