package activity.dashboard

import activity.dashboard.LoginPresenter.Model
import activity.dashboard.common.components.PillButton
import activity.dashboard.common.theme.ActivityDashboardTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import software.amazon.app.platform.inject.ContributesRenderer
import software.amazon.app.platform.renderer.ComposeRenderer

/** Renders the content for [LoginPresenter] on screen using Compose Multiplatform. */
@ContributesRenderer
class LoginRenderer : ComposeRenderer<Model>() {
    private val imageUrls =
        listOf(
            "https://activitydashboard.patchroot.com/image1.png",
            "https://activitydashboard.patchroot.com/image2.png",
            "https://activitydashboard.patchroot.com/image3.png",
        )

    @Composable
    override fun Compose(model: Model) {
        ActivityDashboardTheme {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "Export your Fitbit activities to PDF",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        "Easily export your Fitbit data to PDF format for personal record-keeping, " +
                            "sharing with healthcare providers, or creating custom reports. " +
                            "Track your fitness journey with detailed, exportable summaries of your activities.",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )

                    PreviewImageCarousel(
                        imageUrls = imageUrls,
                        model = model,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                    )

                    PillButton(
                        text = "Log in with Fitbit",
                        onClick = { model.onEvent(LoginPresenter.Event.Login) },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                if (model.expandedImageUrl != null) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.9f))
                                .clickable { model.onEvent(LoginPresenter.Event.SetImage(null)) },
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            AsyncImage(
                                model = model.expandedImageUrl,
                                contentDescription = "Expanded Image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PreviewImageCarousel(
        model: Model,
        imageUrls: List<String>,
        modifier: Modifier = Modifier,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
            ResponsivePreviewImage(
                url = imageUrls[model.currentIndex],
                onClick = { model.onEvent(LoginPresenter.Event.SetImage(imageUrls[model.currentIndex])) },
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                imageUrls.forEachIndexed { index, _ ->
                    val selected = index == model.currentIndex
                    Box(
                        modifier =
                            Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (selected) 12.dp else 8.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(
                                    if (selected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                ),
                    )
                }
            }
        }
    }

    @Composable
    fun ResponsivePreviewImage(
        url: String,
        onClick: () -> Unit = {},
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val maxWidthDp = maxWidth
            val imageWidth =
                when {
                    maxWidthDp < 600.dp -> 0.9f
                    maxWidthDp < 1000.dp -> 0.6f
                    else -> 0.5f
                }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(imageWidth)
                        .aspectRatio(16f / 9f)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { onClick() },
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = url,
                    contentDescription = "Preview Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
