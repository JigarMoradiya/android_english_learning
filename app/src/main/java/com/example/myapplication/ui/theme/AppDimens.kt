package com.example.myapplication.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.DeviceInfo

object AppDimens {
    val isTablet = DeviceInfo.isTablet
    val isLargeTablet = DeviceInfo.isLargeTablet
    // Spacing
    val ToolbarIconSize = if(isLargeTablet) 72.dp else if (isTablet) 64.dp else 42.dp
    val KidsIconSize = if(isLargeTablet) 84.dp else if (isTablet) 72.dp else 56.dp
    val KidIconMedium = if(isLargeTablet) 56.dp else if (isTablet) 48.dp else 40.dp
    val KidIconSmall = if(isLargeTablet) 50.dp else if (isTablet) 40.dp else 30.dp
    val ShadowOffset = if(isLargeTablet) 3.dp else if (isTablet) 2.5.dp else 1.5.dp
    val ShadowOffsetText = if(isLargeTablet) 3.dp else if (isTablet) 2.dp else 1.dp
    val Dimens1 = if(isLargeTablet) 3.dp else if (isTablet) 2.dp else 1.dp
    val Dimens2 = if(isLargeTablet) 4.dp else if (isTablet) 3.dp else 2.dp
    val Dimens3 = if(isLargeTablet) 6.dp else if (isTablet) 4.dp else 3.dp
    val Dimens4 = if(isLargeTablet) 8.dp else if (isTablet) 6.dp else 4.dp
    val Dimens6 = if(isLargeTablet) 12.dp else if (isTablet) 8.dp else 6.dp
    val Dimens8 = if(isLargeTablet) 16.dp else if (isTablet) 12.dp else 8.dp
    val Dimens10 = if(isLargeTablet) 20.dp else if (isTablet) 13.dp else 10.dp
    val Dimens12 = if(isLargeTablet) 24.dp else if (isTablet) 16.dp else 12.dp
    val Dimens16 = if(isLargeTablet) 30.dp else if (isTablet) 22.dp else 16.dp
    val Dimens20 = if(isLargeTablet) 40.dp else if (isTablet) 30.dp else 20.dp
    val Dimens24 = if(isLargeTablet) 48.dp else if (isTablet) 36.dp else 24.dp
    val Dimens28 = if(isLargeTablet) 56.dp else if (isTablet) 36.dp else 28.dp
    val Dimens40 = if(isLargeTablet) 80.dp else if (isTablet) 48.dp else 40.dp
    val Dimens50 = if(isLargeTablet) 100.dp else if (isTablet) 72.dp else 50.dp
    val DimensColorCircles = if(isLargeTablet) 56.dp else if (isTablet) 48.dp else 36.dp
    val CommonPopupImageSize = if(isLargeTablet) 120.dp else if (isTablet) 96.dp else 72.dp
    val ABCDWithImagesSmallImageSize = if(isLargeTablet) 120.dp else if (isTablet) 100.dp else 80.dp
    val MatchLetterBoxSize = if(isLargeTablet) 180.dp else if (isTablet) 150.dp else 100.dp
    val FillBlankLetterBoxSize = if(isLargeTablet) 180.dp else if (isTablet) 144.dp else 72.dp
    val ArrangeLetterInSequenceBoxSize = if(isLargeTablet) 160.dp else if (isTablet) 124.dp else 72.dp
    val DragLetterBoxSize = if(isLargeTablet) 180.dp else if (isTablet) 144.dp else 72.dp
    val MatchWordBoxWidth = if(isLargeTablet) 240.dp else if (isTablet) 190.dp else 124.dp
    val MatchWordBoxHeight = if(isLargeTablet) 72.dp else if (isTablet) 56.dp else 40.dp

    val listenAndAnswerOptionsWidth = if(isLargeTablet) 360.dp else if (isTablet) 330.dp else 240.dp
    val listenAndAnswerOptionsHeight = if(isLargeTablet) 84.dp else if (isTablet) 60.dp else 42.dp
    val articleChoiceHeight = if(isLargeTablet) 84.dp else if (isTablet) 60.dp else 42.dp
    val articleChoiceWidth = if(isLargeTablet) 220.dp else if (isTablet) 180.dp else 140.dp
    val articleChoiceImageHeight = if(isLargeTablet) 200.dp else if (isTablet) 150.dp else 100.dp
    val VocabularyImageHeight = if(isLargeTablet) 150.dp else if (isTablet) 120.dp else 80.dp
    val nounLessonImagesDimension = if(isLargeTablet) 180.dp else if (isTablet) 140.dp else 100.dp
    val nounLessonImagesDimensionNoPadding = if(isLargeTablet) 212.dp else if (isTablet) 164.dp else 116.dp
    val grammarBasicBeginnerMultipleChoiceOptionsHeight = if(isLargeTablet) 84.dp else if (isTablet) 60.dp else 40.dp
    val grammarBasicBeginnerMultipleChoiceOptionsWidth = if(isLargeTablet) 360.dp else if (isTablet) 280.dp else 220.dp
    val grammarBasicOptionsHeight = if(isLargeTablet) 94.dp else if (isTablet) 68.dp else 48.dp
    val grammarBasicOptionsWidth = if(isLargeTablet) 320.dp else if (isTablet) 240.dp else 180.dp
    val grammarBasicPronounOptionsWidth = if(isLargeTablet) 360.dp else if (isTablet) 330.dp else 240.dp

    val AlphabetTracingLetterSize = if(isLargeTablet) 140.sp else if (isTablet) 120.sp else 90.sp
    val LetterRecognitionLetterSize = if(isLargeTablet) 80.sp else if (isTablet) 60.sp else 40.sp
    val MatchWordTextSize = if(isLargeTablet) 24.sp else if (isTablet) 20.sp else 16.sp
    val ABCDWithImagesBigTextSize = if(isLargeTablet) 210.sp else if (isTablet) 180.sp else 120.sp
    val FontExtraLarge36 = if(isLargeTablet) 72.sp else if (isTablet) 54.sp else 36.sp
    val SightWordFont = if(isLargeTablet) 180.sp else if (isTablet) 144.sp else 96.sp

    // ── Bottom sheet constants ───────────────────────────────────────────
    /** Height as a fraction of screen height (same as iOS sheetHeightMultiplier). */
    val SheetHeightFraction = if(isLargeTablet) 0.52f else if (isTablet) 0.55f else 0.58f
    /** Max width cap so sheets don't stretch edge-to-edge on tablets. */
    val SheetMaxWidth = if(isLargeTablet) 660.dp else if (isTablet) 560.dp else 9999.dp
    /** Width fraction — phones get 94% so there's a visible side margin. */
    val SheetWidthFraction = if(isLargeTablet || isTablet) 1f else 0.75f
    /** Circle background behind the main sheet icon. */
    val SheetIconCircle = if(isLargeTablet) 88.dp else if (isTablet) 72.dp else 56.dp
    /** Size of the icon inside the circle. */
    val SheetIconSizeLg = if(isLargeTablet) 42.sp else if (isTablet) 34.sp else 26.sp
    /** Height of the vertical divider between left and right columns. */
    val SheetDividerHeight = if(isLargeTablet) 140.dp else if (isTablet) 120.dp else 100.dp
    /** Width of the vertical divider. */
    val SheetDividerWidth = 1.dp
    /** Drag handle width. */
    val DragHandleWidth = 40.dp
    /** Drag handle height. */
    val DragHandleHeight = 4.dp
}