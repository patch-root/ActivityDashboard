package activity.dashboard

import software.amazon.app.platform.presenter.BaseModel
import software.amazon.app.platform.presenter.molecule.MoleculePresenter

interface AboutPresenter : MoleculePresenter<Unit, AboutPresenter.Model> {
    data class Model(
        val onEvent: (Event) -> Unit,
    ) : BaseModel

    sealed interface Event {
        data object Coffee : Event
    }
}
