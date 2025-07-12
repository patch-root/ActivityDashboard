package activity.dashboard

import software.amazon.app.platform.presenter.BaseModel
import software.amazon.app.platform.presenter.molecule.MoleculePresenter

/**
 * A presenter that hosts other presenters and returns their models. For that reason this presenter
 * doesn't have its own [software.amazon.app.platform.presenter.BaseModel] type and returns [software.amazon.app.platform.presenter.BaseModel].
 */
interface NavigationPresenter : MoleculePresenter<Unit, BaseModel>
