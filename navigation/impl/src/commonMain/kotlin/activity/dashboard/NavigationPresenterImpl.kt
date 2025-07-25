package activity.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import me.tatarka.inject.annotations.Inject
import software.amazon.app.platform.presenter.BaseModel
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class NavigationPresenterImpl(
    private val navigationHeaderPresenter: NavigationHeaderPresenter,
    private val navigationDetailPresenter: NavigationDetailPresenter,
) : NavigationPresenter {
    @Composable
    override fun present(input: Unit): BaseModel {
        val navigationBarModel = navigationHeaderPresenter.present(Unit)
        val navigationDetailModel = navigationDetailPresenter.present(Unit)
        return AppTemplate.HeaderDetailTemplate(navigationBarModel, navigationDetailModel)
    }
}
