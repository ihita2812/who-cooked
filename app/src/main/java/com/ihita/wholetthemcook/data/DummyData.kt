package com.ihita.wholetthemcook.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

suspend fun insertDummyRecipes(recipeDao: RecipeDao) = withContext(Dispatchers.IO) {

    val now = System.currentTimeMillis()

    val recipes = listOf(
        Recipe(
            title = "Butter Chicken",
//            ingredients = listOf(
//                "Chicken – 500g",
//                "Butter – 3 tbsp",
//                "Tomato puree – 1 cup",
//                "Cream – 1/2 cup"
//            ),
            process = "Marinate chicken. Cook gravy. Add chicken and simmer.",
            notes = "Tastes better the next day.",
            dateAdded = Date(now - 2 * 24 * 60 * 60 * 1000),   // 2 days ago
            dateOpened = Date(now - 1 * 60 * 60 * 1000)       // 1 hour ago
        ),

        Recipe(
            title = "Veggie Fried Rice",
//            ingredients = listOf(
//                "Cooked rice – 2 cups",
//                "Mixed vegetables – 1 cup",
//                "Soy sauce – 2 tbsp"
//            ),
            process = "Stir fry veggies. Add rice and sauces.",
            notes = "Use cold rice for best results.",
            dateAdded = Date(now - 5 * 24 * 60 * 60 * 1000),
            dateOpened = Date(now - 2 * 24 * 60 * 60 * 1000)
        ),

        Recipe(
            title = "Chocolate Mug Cake",
//            ingredients = listOf(
//                "Flour – 4 tbsp",
//                "Cocoa powder – 2 tbsp",
//                "Milk – 3 tbsp"
//            ),
            process = "Mix everything. Microwave for 90 seconds.",
            notes = "Do not overcook.",
            dateAdded = Date(now - 1 * 24 * 60 * 60 * 1000),
            dateOpened = Date(now - 30 * 60 * 1000)
        ),

        Recipe(
            title = "Aloo Paratha",
//            ingredients = listOf(
//                "Potatoes – 3",
//                "Wheat flour – 2 cups",
//                "Spices"
//            ),
            process = "Prepare stuffing. Roll and cook on tawa.",
            notes = "Serve with butter or curd.",
            dateAdded = Date(now - 10 * 24 * 60 * 60 * 1000),
            dateOpened = Date(now - 8 * 24 * 60 * 60 * 1000)
        ),

        Recipe(
            title = "Lemon Iced Tea",
//            ingredients = listOf(
//                "Tea bags – 2",
//                "Lemon juice",
//                "Sugar"
//            ),
            process = "Brew tea. Chill. Add lemon and sugar.",
            notes = "Great for summers.",
            dateAdded = Date(now - 7 * 24 * 60 * 60 * 1000),
            dateOpened = Date(now - 3 * 24 * 60 * 60 * 1000)
        ),

        Recipe(
            title = "Zucchini Pasta",
//            ingredients = listOf(
//                "Pasta – 200g",
//                "Zucchini – 1",
//                "Olive oil"
//            ),
            process = "Cook pasta. Sauté zucchini. Mix together.",
            notes = "Light and fresh.",
            dateAdded = Date(now - 3 * 24 * 60 * 60 * 1000),
            dateOpened = Date(now - 5 * 60 * 60 * 1000)
        )
    )

    recipes.forEach { recipeDao.insertRecipe(it) }
}
