package activity.dashboard

import software.amazon.app.platform.presenter.BaseModel
import software.amazon.app.platform.presenter.molecule.MoleculePresenter

interface NavigationHeaderPresenter : MoleculePresenter<Unit, NavigationHeaderPresenter.Model> {
    data class Model(
        val user: User?,
        val expanded: Boolean,
        val onEvent: (Event) -> Unit,
    ) : BaseModel

    sealed interface Event {
        data object ShowAbout : Event

        data object ShowSettings : Event

        data class ToggleUserDropDown(
            val override: Boolean? = null,
        ) : Event

        data object LogOut : Event

        data object NavigateHome : Event
    }
}
