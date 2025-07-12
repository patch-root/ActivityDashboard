package activity.dashboard

import activity.dashboard.SettingsPagePresenter.Model
import activity.dashboard.common.theme.ActivityDashboardTheme
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.amazon.app.platform.inject.ContributesRenderer
import software.amazon.app.platform.renderer.ComposeRenderer

@OptIn(ExperimentalSharedTransitionApi::class)
@ContributesRenderer
class SettingsPageRenderer : ComposeRenderer<Model>() {
    @Composable
    override fun Compose(model: Model) {
        ActivityDashboardTheme {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.75f)
                            .padding(horizontal = 24.dp, vertical = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Data Access Settings",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Text(
                        text =
                            "By default, we route Fitbit data through our secure proxy server. " +
                                "This is necessary because Fitbit's API responses do not include the required CORS headers, " +
                                "which prevents direct access from browsers (especially in WebAssembly environments).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Text(
                        text =
                            "However, if you prefer full direct access to Fitbit's APIs and understand the browser limitations, " +
                                "you can toggle below to bypass our proxy. This preference is stored locally in your browser.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    )

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = if (model.proxyEnabled) "Using Proxy Server" else "Using Fitbit APIs Directly",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Switch(
                            checked = model.proxyEnabled,
                            onCheckedChange = { model.onEvent(SettingsPagePresenter.Event.ToggleProxy(it)) },
                        )
                    }
                }
            }
        }
    }
}
