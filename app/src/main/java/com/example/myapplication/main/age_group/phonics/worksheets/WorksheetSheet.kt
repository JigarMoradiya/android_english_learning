package com.example.myapplication.main.age_group.phonics.worksheets

import androidx.compose.runtime.Composable

/** One finished worksheet: the page chrome with the right exercise inside it.
 *  Mirrors iOS `WorksheetSheet`. Unit 1 dispatches only the letter page. */
@Composable
fun WorksheetSheet(
    spec: WorksheetSpec,
    tappable: Boolean = true,
    onWordTap: (word: String, letter: String) -> Unit = { _, _ -> }
) {
    val theme = WorksheetCatalog.theme(spec)
    WorksheetPage(spec = spec, theme = theme) {
        when (val body = spec.body) {
            is WorksheetBody.LetterPage ->
                WorksheetLetterPageBody(
                    theme = theme,
                    letter = body.letter,
                    tappable = tappable,
                    onWordTap = onWordTap
                )
            is WorksheetBody.LetterRead ->
                WorksheetLetterReadBody(theme, body.letters, body.columns, body.showsSound)
            is WorksheetBody.CircleLetter ->
                WorksheetCircleLetterBody(theme, body.items)
            is WorksheetBody.MatchLetterImage ->
                WorksheetMatchLetterImageBody(theme, body.items)
            is WorksheetBody.LetterMatcher ->
                WorksheetLetterMatcherBody(theme, body.pairs)
        }
    }
}
