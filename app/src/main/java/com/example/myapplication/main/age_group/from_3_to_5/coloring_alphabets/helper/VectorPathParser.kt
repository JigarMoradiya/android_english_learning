package com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.helper

import android.content.Context
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import org.xmlpull.v1.XmlPullParser

object VectorPathParser {

    private val cache = mutableMapOf<String, Path>()

    fun getPath(context: Context, drawableName: String): Path {

        // ✅ cache (important for performance)
        cache[drawableName]?.let { return it }

        val resId = context.resources.getIdentifier(
            drawableName,
            "drawable",
            context.packageName
        )

        val parser = context.resources.getXml(resId)

        var pathData: String? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG &&
                parser.name == "path"
            ) {
                pathData = parser.getAttributeValue(
                    "http://schemas.android.com/apk/res/android",
                    "pathData"
                )
                break
            }
            parser.next()
        }

        if (pathData.isNullOrEmpty()) {
            return Path()
        }

        val path = PathParser().parsePathString(pathData).toPath()

        cache[drawableName] = path
        return path
    }
}