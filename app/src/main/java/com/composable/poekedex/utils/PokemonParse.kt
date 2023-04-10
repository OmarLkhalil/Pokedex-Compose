package com.composable.poekedex.utils

import androidx.compose.ui.graphics.Color
import com.composable.poekedex.data.remote.resposnes.Stat
import com.composable.poekedex.data.remote.resposnes.Type
import com.composable.poekedex.ui.theme.*
import java.util.*


/**

Returns a color corresponding to the given type. If the type is not recognized, returns Color.Black.
@param type The Pokemon type to parse into a color.
@return A color corresponding to the given type.
 */
fun parseTypeToColor(type: Type): Color {
    return when (type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

/**

Returns a color corresponding to the given stat. If the stat is not recognized, returns Color.White.
@param stat The Pokemon stat to parse into a color.
@return A color corresponding to the given stat.
 */
fun parseStatToColor(stat: Stat): Color {
    return when (stat.stat.name.lowercase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

/**

Returns the abbreviation of the given stat. If the stat is not recognized, returns an empty string.
@param stat The Pokemon stat to parse into an abbreviation.
@return The abbreviation of the given stat.
 */
fun parseStatToAbbr(stat: Stat): String {
    return when (stat.stat.name.lowercase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}