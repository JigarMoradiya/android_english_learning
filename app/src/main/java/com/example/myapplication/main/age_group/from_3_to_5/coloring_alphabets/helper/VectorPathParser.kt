package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import org.xmlpull.v1.XmlPullParser
import androidx.core.graphics.toColorInt

object VectorPathParser {

    data class VectorData(
        val path: Path,
        val color: Color
    )

    private val cache = mutableMapOf<String, VectorData>()

    fun getPath(
        context: Context,
        drawableRes: Int,
        drawableName: String
    ): VectorData {

        cache[drawableName]?.let { return it }

        val parser = context.resources.getXml(drawableRes)

        var pathData: String? = null
        var fillColorStr: String? = null
        var strokeColorStr: String? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG &&
                parser.name == "path"
            ) {

                pathData = parser.getAttributeValue(
                    "http://schemas.android.com/apk/res/android",
                    "pathData"
                )

                fillColorStr = parser.getAttributeValue(
                    "http://schemas.android.com/apk/res/android",
                    "fillColor"
                )

                strokeColorStr = parser.getAttributeValue(
                    "http://schemas.android.com/apk/res/android",
                    "strokeColor"
                )

                break
            }
            parser.next()
        }

        if (pathData.isNullOrEmpty()) {
            return VectorData(Path(), Color.Black)
        }

        val path = PathParser().parsePathString(pathData).toPath()

        val color = parseColor(context, fillColorStr, strokeColorStr)

        val result = VectorData(path, color)

        cache[drawableName] = result
        return result
    }

    private fun parseColor(
        context: Context,
        fill: String?,
        stroke: String?
    ): Color {

        fun resolve(colorStr: String?): Color? {
            if (colorStr.isNullOrEmpty()) return null

            return try {
                when {
                    colorStr.startsWith("#") -> {
                        Color(colorStr.toColorInt())
                    }

                    colorStr.startsWith("@") -> {
                        val resId = context.resources.getIdentifier(
                            colorStr.substringAfter("/"),
                            "color",
                            context.packageName
                        )
                        Color(context.getColor(resId))
                    }

                    else -> null
                }
            } catch (e: Exception) {
                null
            }
        }

        // priority: fill → stroke → fallback
        return resolve(fill)
            ?: resolve(stroke)
            ?: Color.Black
    }
}