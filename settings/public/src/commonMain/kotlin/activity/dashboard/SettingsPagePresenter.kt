package activity.dashboard

import activity.dashboard.SettingsPagePresenter.Model
import software.amazon.app.platform.presenter.BaseModel
import software.amazon.app.platform.presenter.molecule.MoleculePresenter

interface SettingsPagePresenter : MoleculePresenter<Unit, Model> {
    data class Model(
        val proxyEnabled: Boolean,
        val onEvent: (Event) -> Unit,
    ) : BaseModel

    sealed interface Event {
        data class ToggleProxy(
            val enabled: Boolean,
        ) : Event
    }
}
