package com.example.firstandroidapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val name: String,
    val brand: String,
    val nutriScore: NutriScore,
    val barcode: String,
    val thumbnail: String,
    val quantity: String,
    val countries: List<String>,
    val ingredients: List<String>,
    val allergens: List<String>,
    val additives: List<String>,
) : Parcelable

enum class NutriScore(val label: String) {
    A("A"), B("B"), C("C"), D("D"), E("E"), Unknown("")
}

fun generateFakeProduct() = Product(
    name = "Petits pois et carottes",
    brand = "Cassegrain",
    nutriScore = NutriScore.A,
    barcode = "3083680085304",
    thumbnail = "https://static.openfoodfacts.org/images/products/308/368/008/5304/front_fr.7.400.jpg",
    quantity = "400g",
    countries = listOf("France", "Japon", "Suisse"),
    ingredients = listOf(
        "Petits pois 66%",
        "Eau",
        "Garniture 2,8% (salade, oignon grelot)",
        "Sucre",
        "Sel",
        "Arôme naturel"
    ),
    allergens = emptyList(),
    additives = emptyList(),
)