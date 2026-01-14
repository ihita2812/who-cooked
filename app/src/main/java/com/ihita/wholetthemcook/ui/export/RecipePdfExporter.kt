package com.ihita.wholetthemcook.ui.export

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.res.ResourcesCompat
import com.ihita.wholetthemcook.R
import java.io.OutputStream

import com.ihita.wholetthemcook.data.ExportRecipe

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

    private val DARK_PURPLE = Color.parseColor("#5B3E85")
    private val LIGHTER_PURPLE = Color.parseColor("#7D5BA6")
    private val TEXT_BODY = Color.parseColor("#4B3A61")
    private val DIVIDER_PURPLE = Color.parseColor("#D6C9FF")

    fun export(context: Context, recipes: List<ExportRecipe>, outputStream: OutputStream) {

        val document = PdfDocument()

        val titleFont = ResourcesCompat.getFont(context, R.font.playfairdisplay_extrabold) ?: Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val sectionFont = ResourcesCompat.getFont(context, R.font.raleway_medium) ?: Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val bodyFont = ResourcesCompat.getFont(context, R.font.nunito_regular) ?: Typeface.DEFAULT

        val titlePaint = Paint().apply {
            textSize = 22f
            typeface = titleFont
            color = DARK_PURPLE
            isAntiAlias = true
        }

        val sectionPaint = Paint().apply {
            textSize = 13.5f
            typeface = sectionFont
            color = LIGHTER_PURPLE
            isAntiAlias = true
            letterSpacing = 0.1f
        }

        val bodyPaint = Paint().apply {
            textSize = 14f
            typeface = bodyFont
            color = TEXT_BODY
            isAntiAlias = true
        }

        val accentPaint = Paint().apply {
            color = DIVIDER_PURPLE
            isAntiAlias = true
        }

        var pageNumber = 1

        recipes.forEach { recipe ->
            val pageInfo = PdfDocument.PageInfo
                .Builder(595, 842, pageNumber++)
                .create()

            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            var y = 48f
            val marginStart = 40f

            canvas.drawText(recipe.title, marginStart, y, titlePaint)
            y += 20f

            canvas.drawRoundRect(
                marginStart,
                y,
                marginStart + 72f,
                y + 3f,
                4f,
                4f,
                accentPaint
            )

            y += 36f

            canvas.drawText("INGREDIENTS", marginStart, y, sectionPaint)
            y += 22f

            recipe.ingredients.forEach { ingredient ->
                canvas.drawText("•", marginStart, y, bodyPaint)

                val ing = buildString {
                    append(ingredient.name)
                    ingredient.quantity?.let { q ->
                        append(" – ")
                        append(q)
                        ingredient.unit?.let { unit ->
                            append(" $unit")
                        }
                    }
                    ingredient.notes
                        ?.takeIf { it.isNotBlank() }
                        ?.let { note ->
                            append(" ($note)")
                        }
                }

                canvas.drawText(ing, marginStart + 15f, y, bodyPaint)
                y += 20f
            }

            y += 14f

            canvas.drawText("PROCESS", marginStart, y, sectionPaint)
            y += 22f

            recipe.process.forEachIndexed { index, step ->
                canvas.drawText("${index + 1}.", marginStart, y, Paint(bodyPaint).apply {
                    typeface = titleFont
                })

                canvas.drawText(step, marginStart + 24f, y, bodyPaint)

                y += 22f
            }

            recipe.notes
                ?.takeIf { it.isNotBlank() }
                ?.let { notes ->
                    y += 14f
                    canvas.drawText("NOTES", marginStart, y, sectionPaint)
                    y += 22f
                    canvas.drawText(notes, marginStart, y, bodyPaint)
                }

            document.finishPage(page)
        }

        document.writeTo(outputStream)
        document.close()
    }
}
