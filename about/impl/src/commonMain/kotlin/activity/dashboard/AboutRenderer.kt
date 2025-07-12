package activity.dashboard

import activity.dashboard.AboutPresenter.Model
import activity.dashboard.common.theme.ActivityDashboardTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import software.amazon.app.platform.inject.ContributesRenderer
import software.amazon.app.platform.renderer.ComposeRenderer

@OptIn(ExperimentalMaterial3Api::class)
@ContributesRenderer
class AboutRenderer : ComposeRenderer<Model>() {
    @Composable
    override fun Compose(model: Model) {
        ActivityDashboardTheme {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 32.dp),
            ) {
                SectionTitle("About")
                Spacer(Modifier.height(24.dp))

                Section(
                    title = "How it was made",
                    body = AboutText.howItWasMade,
                )

                SectionDivider()

                Section(
                    title = "Architecture",
                    body = AboutText.architecture,
                )

                SectionDivider()

                Section(
                    title = "Privacy Policy",
                    body = AboutText.privacyPolicy,
                )

                SectionDivider()

                Section(
                    title = "Contact",
                    body = AboutText.contact,
                )

                Spacer(modifier = Modifier.height(48.dp)) // Padding for bottom safe area
            }
        }
    }

    @Composable
    private fun Section(
        title: String,
        body: String,
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            SectionTitle(title)
            Spacer(modifier = Modifier.height(8.dp))
            SectionBody(body)
        }
    }

    @Composable
    private fun SectionTitle(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }

    @Composable
    private fun SectionBody(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
        )
    }

    @Composable
    private fun SectionDivider() {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
        )
    }
}
