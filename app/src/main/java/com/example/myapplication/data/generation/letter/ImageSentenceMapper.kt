package com.example.myapplication.data.generation.letter

import com.example.myapplication.R

object ImageSentenceMapper {

    private val map = mapOf(

        // Colors & Shapes
        "red_ball" to R.drawable.red_ball,
        "blue_sky" to R.drawable.blue_sky,
        "yellow_star" to R.drawable.yellow_star,
        "green_tree" to R.drawable.green_tree,
        "circle_shape" to R.drawable.circle_shape,
        "square_shape" to R.drawable.square_shape,
        "triangle_shape" to R.drawable.triangle_shape,
        "rectangle_shape" to R.drawable.rectangle_shape,
        "colorful_balloons" to R.drawable.colorful_balloons,
        "rainbow_colors" to R.drawable.rainbow_colors,

        // Community
        "doctor_helping" to R.drawable.doctor_helping,
        "teacher_classroom" to R.drawable.teacher_classroom,
        "police_officer" to R.drawable.police_officer,
        "firefighter" to R.drawable.firefighter,
        "farmer_field" to R.drawable.farmer_field,
        "postman_delivery" to R.drawable.postman_delivery,
        "nurse_hospital" to R.drawable.nurse_hospital,
        "shopkeeper_store" to R.drawable.shopkeeper_store,
        "driver_bus" to R.drawable.driver_bus,
        "cleaner_sweeping" to R.drawable.cleaner_sweeping,

        // Daily Routine
        "waking_up" to R.drawable.waking_up,
        "brushing_teeth" to R.drawable.brushing_teeth,
        "getting_dressed" to R.drawable.getting_dressed,
        "going_to_school" to R.drawable.going_to_school,
        "doing_homework" to R.drawable.doing_homework,
        "playing_evening" to R.drawable.playing_evening,
        "eating_dinner" to R.drawable.eating_dinner,
        "watching_tv" to R.drawable.watching_tv,
        "reading_book" to R.drawable.reading_book,
        "sleeping_night" to R.drawable.sleeping_night,

        // Emotions
        "happy_child" to R.drawable.happy_child,
        "sad_child" to R.drawable.sad_child,
        "angry_child" to R.drawable.angry_child,
        "excited_child" to R.drawable.excited_child,
        "scared_child" to R.drawable.scared_child,
        "surprised_child" to R.drawable.surprised_child,
        "tired_child" to R.drawable.tired_child,
        "proud_child" to R.drawable.proud_child,
        "worried_child" to R.drawable.worried_child,
        "kind_child" to R.drawable.kind_child,

        // Family
        "family_photo" to R.drawable.family_photo,
        "mother_child" to R.drawable.mother_child,
        "father_child" to R.drawable.father_child,
        "brother_sister" to R.drawable.brother_sister,
        "sister_helping" to R.drawable.sister_helping,
        "friends_playing" to R.drawable.friends_playing,
        "grandparents_home" to R.drawable.grandparents_home,
        "birthday_family" to R.drawable.birthday_family,
        "family_helping" to R.drawable.family_helping,
        "friends_sharing" to R.drawable.friends_sharing,

        // Festivals
        "birthday_party" to R.drawable.birthday_party,
        "diwali_lights" to R.drawable.diwali_lights,
        "christmas_tree" to R.drawable.christmas_tree,
        "holi_colors" to R.drawable.holi_colors,
        "independence_day" to R.drawable.independence_day,
        "new_year_celebration" to R.drawable.new_year_celebration,
        "family_gathering" to R.drawable.family_gathering,
        "school_function" to R.drawable.school_function,
        "wedding_ceremony" to R.drawable.wedding_ceremony,
        "picnic_day" to R.drawable.picnic_day,

        // Food
        "apple_fruit" to R.drawable.apple_fruit,
        "banana_fruit" to R.drawable.banana_fruit,
        "breakfast_table" to R.drawable.breakfast_table,
        "lunch_box" to R.drawable.lunch_box,
        "orange_fruit" to R.drawable.orange_fruit,
        "vegetables_plate" to R.drawable.vegetables_plate,
        "milk_glass" to R.drawable.milk_glass,
        "birthday_cake" to R.drawable.birthday_cake,
        "ice_cream" to R.drawable.ice_cream,
        "family_dinner" to R.drawable.family_dinner,

        // Home Life
        "family_eating_dinner" to R.drawable.family_eating_dinner,
        "girl_cleaning_room" to R.drawable.girl_cleaning_room,
        "mother_cooking" to R.drawable.mother_cooking,
        "boy_watching_tv" to R.drawable.boy_watching_tv,
        "pet_dog_home" to R.drawable.pet_dog_home,
        "family_cleaning" to R.drawable.family_cleaning,
        "girl_reading_home" to R.drawable.girl_reading_home,
        "father_fixing_chair" to R.drawable.father_fixing_chair,
        "family_prayer" to R.drawable.family_prayer,
        "girl_sleeping" to R.drawable.girl_sleeping,

        // Nature
        "dog_in_park" to R.drawable.dog_in_park,
        "cat_on_tree" to R.drawable.cat_on_tree,
        "butterfly_garden" to R.drawable.butterfly_garden,
        "elephant_zoo" to R.drawable.elephant_zoo,
        "bird_on_branch" to R.drawable.bird_on_branch,
        "river_flowing" to R.drawable.river_flowing,
        "rainy_forest" to R.drawable.rainy_forest,
        "mountain_view" to R.drawable.mountain_view,
        "cow_in_field" to R.drawable.cow_in_field,
        "sunset_beach" to R.drawable.sunset_beach,

        // Play and Fun
        "boy_playing_ball" to R.drawable.boy_playing_ball,
        "girl_flying_kite" to R.drawable.girl_flying_kite,
        "boy_riding_bicycle" to R.drawable.boy_riding_bicycle,
        "kids_playing_football" to R.drawable.kids_playing_football,
        "boy_building_blocks" to R.drawable.boy_building_blocks,
        "girl_skipping_rope" to R.drawable.girl_skipping_rope,
        "kids_playing_slide" to R.drawable.kids_playing_slide,
        "toy_car_race" to R.drawable.toy_car_race,
        "kids_playing_swing" to R.drawable.kids_playing_swing,
        "kids_solving_puzzle" to R.drawable.kids_solving_puzzle,

        // School Life
        "kids_in_class" to R.drawable.kids_in_class,
        "girl_reading_book" to R.drawable.girl_reading_book,
        "boy_writing" to R.drawable.boy_writing,
        "school_playground" to R.drawable.school_playground,
        "teacher_helping_student" to R.drawable.teacher_helping_student,
        "school_library" to R.drawable.school_library,
        "drawing_class" to R.drawable.drawing_class,
        "school_assembly" to R.drawable.school_assembly,
        "science_class" to R.drawable.science_class,
        "school_bus_home" to R.drawable.school_bus_home,

        // Sports
        "playing_football" to R.drawable.playing_football,
        "playing_cricket" to R.drawable.playing_cricket,
        "playing_basketball" to R.drawable.playing_basketball,
        "running_race" to R.drawable.running_race,
        "swimming_pool" to R.drawable.swimming_pool,
        "cycling_park" to R.drawable.cycling_park,
        "yoga_class" to R.drawable.yoga_class,
        "badminton_game" to R.drawable.badminton_game,
        "skipping_rope" to R.drawable.skipping_rope,
        "playing_chess" to R.drawable.playing_chess,

        // Transport
        "red_car" to R.drawable.red_car,
        "school_bus" to R.drawable.school_bus,
        "train_station" to R.drawable.train_station,
        "airplane_sky" to R.drawable.airplane_sky,
        "bicycle_ride" to R.drawable.bicycle_ride,
        "boat_river" to R.drawable.boat_river,
        "traffic_signal" to R.drawable.traffic_signal,
        "motorcycle_ride" to R.drawable.motorcycle_ride,
        "airport_scene" to R.drawable.airport_scene,
        "family_trip" to R.drawable.family_trip,

        // Weather
        "sunny_day" to R.drawable.sunny_day,
        "rainy_day" to R.drawable.rainy_day,
        "cloudy_day" to R.drawable.cloudy_day,
        "windy_day" to R.drawable.windy_day,
        "stormy_weather" to R.drawable.stormy_weather,
        "snowy_day" to R.drawable.snowy_day,
        "hot_day" to R.drawable.hot_day,
        "cold_day" to R.drawable.cold_day,
        "rainbow_after_rain" to R.drawable.rainbow_after_rain,
        "spring_weather" to R.drawable.spring_weather,
    )

    fun get(name: String?): Int? {
        val key = name
            ?.trim()
            ?.lowercase()
            ?.replace("-", "_")
            ?.replace(" ", "_")
            ?: return null

        return map[key]
    }
}