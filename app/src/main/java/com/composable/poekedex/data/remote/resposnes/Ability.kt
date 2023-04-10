package com.composable.poekedex.data.remote.resposnes


import com.google.gson.annotations.SerializedName

data class Ability(
    val ability: AbilityX,
    @SerializedName("is_hidden")
    val isHidden: Boolean,
    val slot: Int
)