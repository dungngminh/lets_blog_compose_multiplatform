package me.dungngminh.lets_blog_kmp.commons.extensions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

const val TAB_WIDTH_IN_SPACES = 4
val tabsVisualTransformation =
    VisualTransformation { original ->
        val originalTabPositions = mutableListOf<Int>() // indexes of tabs in original string
        val builder = StringBuilder()
        for (offset in 0 until original.text.length) {
            val char = original.text[offset]
            if (char == '\t') {
                for (i in 0 until TAB_WIDTH_IN_SPACES) {
                    builder.append(' ')
                }
                originalTabPositions.add(offset)
            } else {
                builder.append(char)
            }
        }

        TransformedText(
            AnnotatedString(builder.toString()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int =
                    offset + originalTabPositions.count { it < offset } * (TAB_WIDTH_IN_SPACES - 1)

                override fun transformedToOriginal(offset: Int): Int {
                    var resultOffset = offset
                    for (i in originalTabPositions) {
                        val newOffset = resultOffset - (TAB_WIDTH_IN_SPACES - 1)
                        if (i in newOffset..resultOffset) {
                            return i
                        } else if (i < newOffset) {
                            resultOffset = newOffset
                        }
                    }
                    return resultOffset
                }
            },
        )
    }
