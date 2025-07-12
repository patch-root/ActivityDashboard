package activity.dashboard

import activity.dashboard.UserPagePresenter.Model
import activity.dashboard.fitbit.Activity
import activity.dashboard.fitbit.FitbitActivitiesResponse
import software.amazon.app.platform.presenter.BaseModel
import software.amazon.app.platform.presenter.molecule.MoleculePresenter

interface UserPagePresenter : MoleculePresenter<Unit, Model> {
    data class Model(
        val fitbitActivities: Loadable<FitbitActivitiesResponse>,
        val currentMonthOffset: Int,
        val onEvent: (Event) -> Unit,
    ) : BaseModel

    sealed interface Event {
        data class Export(
            val activities: List<Activity>,
        ) : Event

        data object PreviousMonth : Event

        data object NextMonth : Event

        data object Retry : Event
    }

    sealed class Loadable<out T> {
        object Loading : Loadable<Nothing>()

        data class Success<T>(val data: T) : Loadable<T>()

        data class Error(val message: String) : Loadable<Nothing>()

        val isLoading get() = this is Loading
        val isSuccess get() = this is Success<*>
        val isError get() = this is Error
    }
}
