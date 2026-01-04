package com.ihita.wholetthemcook.ui.export

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.ihita.wholetthemcook.data.ExportRecipe
import java.io.OutputStream

/**
 * Data models used ONLY for export (decoupled from Room)
 */

//data class ExportRecipe(
//    val title: String,
//    val process: List<String>,
//    val notes: String?,
//    val ingredients: List<ExportIngredient>
//)
//
//data class ExportIngredient(
//    val name: String,
//    val quantity: Float?,
//    val unit: String?,
//    val notes: String?
//)

/**
 * PDF Exporter
 */
object RecipePdfExporter {

    fun export(recipes: List<ExportRecipe>, outputStream: OutputStream) {
        val document = PdfDocument()

        val titlePaint = Paint().apply {
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            color = Color.BLACK
            isAntiAlias = true
        }

        val headerPaint = Paint().apply {
            textSize = 16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            color = Color.DKGRAY
            isAntiAlias = true
        }

        val bodyPaint = Paint().apply {
            textSize = 14f
            color = Color.BLACK
            isAntiAlias = true
        }

        var pageNumber = 1

        recipes.forEach { recipe ->
            val pageInfo = PdfDocument.PageInfo
                .Builder(595, 842, pageNumber++)
                .create()

            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            var y = 40f

            // Title
            canvas.drawText(recipe.title, 40f, y, titlePaint)
            y += 32f

            // Ingredients
            canvas.drawText("Ingredients", 40f, y, headerPaint)
            y += 22f

            recipe.ingredients.forEach { ingredient ->
                val line = buildString {
                    append("• ${ingredient.name}")
                    ingredient.quantity?.let { q ->
                        append(" – $q")
                        ingredient.unit?.let { u -> append(" $u") }
                    }
                    if (ingredient.notes != null && ingredient.notes != "") {
                        append(" (${ingredient.notes})")
                    }
                }

                canvas.drawText(line, 50f, y, bodyPaint)
                y += 18f
            }

            y += 16f

            // Process
            canvas.drawText("Process", 40f, y, headerPaint)
            y += 22f

            recipe.process.forEachIndexed { index, step ->
                canvas.drawText(
                    "${index + 1}. $step",
                    50f,
                    y,
                    bodyPaint
                )
                y += 18f
            }

            // Notes
            recipe.notes
                ?.takeIf { it.isNotBlank() }
                ?.let { notes ->
                    y += 16f
                    canvas.drawText("Notes", 40f, y, headerPaint)
                    y += 22f
                    canvas.drawText(notes, 50f, y, bodyPaint)
                }

            document.finishPage(page)
        }

        document.writeTo(outputStream)
        document.close()
    }
}
