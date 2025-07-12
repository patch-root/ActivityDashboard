package activity.dashboard

import software.amazon.app.platform.presenter.BaseModel
import software.amazon.app.platform.presenter.molecule.MoleculePresenter

/** A presenter to render the login screen. */
interface LoginPresenter : MoleculePresenter<Unit, LoginPresenter.Model> {
    /** The state of the login screen. */
    data class Model(
        val currentIndex: Int,
        val expandedImageUrl: String?,
        val onEvent: (Event) -> Unit,
    ) : BaseModel

    sealed interface Event {
        data object Login : Event

        data class SetImage(
            val imageUrl: String?,
        ) : Event
    }
}
