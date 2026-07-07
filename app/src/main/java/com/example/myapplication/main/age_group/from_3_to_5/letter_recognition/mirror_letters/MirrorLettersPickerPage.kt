package com.example.myapplication.main.age_group.from_3_to_5.letter_recognition.mirror_letters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.utils.AudioPlayerManager

// One pair at a time on purpose — showing all four letters together is exactly
// the kind of clutter that makes this confusion worse, not better.
@Composable
fun MirrorLettersPickerPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.purpleBlue, shape = KidsFloatingShape.stars)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = stringResource(R.string.menu_tricky_twins),
                expandWidth = false,
                onBackClick = { navController.popBackStack() }
            )

            Text(
                text = stringResource(R.string.tricky_twins_pick_a_pair),
                style = MaterialTheme.typography.headlineLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens16)
            )

            Spacer(modifier = Modifier.height(Dimens16))

            // weight(1f) here (not a Spacer before/after) so the grid actually
            // gets bounded height and scrolls instead of clipping its 2nd row
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = Dimens24),
                horizontalArrangement = Arrangement.spacedBy(Dimens16),
                verticalArrangement = Arrangement.spacedBy(Dimens16),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = Dimens16)
            ) {
                items(MirrorLetterPair.entries) { pair ->
                    PairCard(pair) {
                        AudioPlayerManager.playSoundMenuClick()
                        navController.navigate(RouteNavigation.MirrorLettersIntro.createRoute(pair.name))
                    }
                }
            }
        }
    }
}

@Composable
private fun PairCard(pair: MirrorLetterPair, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp), ambientColor = pair.accentColor.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(vertical = Dimens24),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = pair.title,
            style = MaterialTheme.typography.displayMedium.scaled(),
            fontWeight = FontWeight.Black,
            color = pair.accentColor
        )
    }
}
