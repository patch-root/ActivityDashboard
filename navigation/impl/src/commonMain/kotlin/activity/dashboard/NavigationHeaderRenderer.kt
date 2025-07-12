package activity.dashboard

import activity.dashboard.NavigationHeaderPresenter.Model
import activity.dashboard.common.theme.ActivityDashboardTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Stairs
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import software.amazon.app.platform.inject.ContributesRenderer
import software.amazon.app.platform.renderer.ComposeRenderer

/** Renders the content for [LoginPresenter] on screen using Compose Multiplatform. */
@ContributesRenderer
class NavigationHeaderRenderer : ComposeRenderer<Model>() {
    @Composable
    override fun Compose(model: Model) {
        ActivityDashboardTheme {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Stairs,
                            contentDescription = "Dashboard Icon",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Activity Dashboard",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Home",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier =
                                Modifier
                                    .padding(horizontal = 8.dp)
                                    .clickable { model.onEvent(NavigationHeaderPresenter.Event.NavigateHome) },
                        )

                        IconButton(
                            onClick = { model.onEvent(NavigationHeaderPresenter.Event.ShowSettings) },
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp),
                            )
                        }

                        IconButton(
                            onClick = { model.onEvent(NavigationHeaderPresenter.Event.ShowAbout) },
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Help,
                                contentDescription = "Help",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp),
                            )
                        }

                        model.user?.let { user ->
                            Box(
                                modifier = Modifier.wrapContentSize(Alignment.TopEnd),
                            ) {
                                IconButton(
                                    // TODO Should just trigger a little drop down menu to allow logging out.
                                    onClick = { model.onEvent(NavigationHeaderPresenter.Event.ToggleUserDropDown()) },
                                    modifier = Modifier.size(24.dp),
                                ) {
                                    AsyncImage(
                                        model = user.profileImage,
                                        contentDescription = "Profile Picture",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.extraSmall),
                                    )
                                }
                                DropdownMenu(
                                    expanded = model.expanded,
                                    onDismissRequest = { model.onEvent(NavigationHeaderPresenter.Event.ToggleUserDropDown(false)) },
                                ) {
                                    Text(
                                        text = "Signed in as",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Text(
                                        text = user.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                                    DropdownMenuItem(
                                        text = { Text("Log out") },
                                        onClick = {
                                            model.onEvent(NavigationHeaderPresenter.Event.LogOut)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.onSecondary),
                )
            }
        }
    }

    @Composable
    fun ProfileDropdown(
        userName: String,
        onLogout: () -> Unit,
    ) {
    }
}
