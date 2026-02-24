package com.example.travelsafe.repository

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.travelsafe.model.RecommendedPlace
import java.util.concurrent.Executors

class PlaceRepoImpl : PlaceRepo {

    // ── Replace these with your actual Cloudinary credentials ──
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to " djjyufbxz",
            "api_key"    to "888428925711334",
            "api_secret" to "Me1PsvbZx1YJ0r5hyEztxSeuW7w"
        )
    )

    // All places data
    private val places = listOf(
        RecommendedPlace(
            name = "Royal Coffee Shop",
            imageUrl = "https://res.cloudinary.com/your_cloud_name/image/upload/royal_coffee.jpg",
            location = "Thamel, Kathmandu",
            description = "A cozy and iconic coffee shop nestled in the heart of Thamel. Famous for its artisan brews, warm ambiance, and stunning mountain views from the rooftop terrace.",
            price = "$5/cup",
            rating = 4.5f,
            reviews = 220,
            highlights = listOf("Rooftop mountain view", "Artisan coffee & pastries", "Free WiFi", "Live acoustic music on weekends")
        ),
        RecommendedPlace(
            name = "Royal Beach",
            imageUrl = "https://res.cloudinary.com/your_cloud_name/image/upload/royal_beach.jpg",
            location = "Pokhara Lakeside",
            description = "A pristine lakeside beach with crystal clear waters and breathtaking views of the Annapurna mountain range. Perfect for kayaking, swimming, and sunset watching.",
            price = "$10/person",
            rating = 4.8f,
            reviews = 340,
            highlights = listOf("Annapurna mountain views", "Kayaking & water sports", "Sunset viewpoint", "Beachside restaurants")
        ),
        RecommendedPlace(
            name = "Mountain View",
            imageUrl = "https://res.cloudinary.com/your_cloud_name/image/upload/mountain_view.jpg",
            location = "Nagarkot, Bhaktapur",
            description = "One of the best spots in Nepal to witness the Himalayan sunrise. The panoramic view stretches from Dhaulagiri in the west to Kanchenjunga in the east.",
            price = "$15/person",
            rating = 4.7f,
            reviews = 190,
            highlights = listOf("Himalayan sunrise view", "360° panoramic viewpoint", "Hiking trails", "Local cultural experience")
        ),
        RecommendedPlace(
            name = "Pashupatinath Temple",
            imageUrl = "https://res.cloudinary.com/your_cloud_name/image/upload/pashupatinath.jpg",
            location = "Kathmandu",
            description = "One of the most sacred Hindu temples in the world, dedicated to Lord Shiva. Located on the banks of the Bagmati River, it is a UNESCO World Heritage Site.",
            price = "Free",
            rating = 4.9f,
            reviews = 520,
            highlights = listOf("UNESCO World Heritage Site", "Sacred Hindu temple", "Bagmati riverside ghats", "Ancient architecture")
        ),
        RecommendedPlace(
            name = "Boudhanath Stupa",
            imageUrl = "https://res.cloudinary.com/your_cloud_name/image/upload/boudhanath.jpg",
            location = "Kathmandu",
            description = "One of the largest stupas in the world and a major pilgrimage site for Tibetan Buddhists. The all-seeing eyes of Buddha watch over the city from its tower.",
            price = "$3/person",
            rating = 4.8f,
            reviews = 450,
            highlights = listOf("Largest stupa in Nepal", "Tibetan Buddhist culture", "Surrounding monasteries", "Peaceful walking circuit")
        )
    )

    override fun getAllPlaces(
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        try {
            callback(true, "Done", places)
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to load places", emptyList())
        }
    }

    override fun getPlaceByName(
        name: String,
        callback: (Boolean, String, RecommendedPlace?) -> Unit
    ) {
        try {
            val place = places.find { it.name.equals(name, ignoreCase = true) }
            if (place != null) {
                callback(true, "Done", place)
            } else {
                callback(false, "Place not found", null)
            }
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to find place", null)
        }
    }

    override fun searchPlaces(
        query: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        try {
            if (query.isEmpty()) {
                callback(true, "Done", places)
                return
            }
            val result = places.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
            callback(true, "Done", result)
        } catch (e: Exception) {
            callback(false, e.message ?: "Search failed", emptyList())
        }
    }

    override fun getTopRatedPlaces(
        limit: Int,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        try {
            val topRated = places.sortedByDescending { it.rating }.take(limit)
            callback(true, "Done", topRated)
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to load places", emptyList())
        }
    }

    override fun getPlacesByLocation(
        location: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        try {
            val filtered = places.filter {
                it.location.contains(location, ignoreCase = true)
            }
            callback(true, "Done", filtered)
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to load places", emptyList())
        }
    }

    // ── Image upload to Cloudinary (same pattern as teacher) ──
    override fun uploadPlaceImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)

                val result = cloudinary.uploader().upload(
                    inputStream,
                    ObjectUtils.asMap(
                        "resource_type", "image"
                    )
                )

                val imageUrl = result["secure_url"] as String?

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }
}