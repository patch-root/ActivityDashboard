package activity.dashboard

import activity.dashboard.animation.AnimationContentKey
import activity.dashboard.animation.AnimationContentKey.Companion.contentKey
import software.amazon.app.platform.presenter.BaseModel
import software.amazon.app.platform.presenter.template.Template

/** All [Template]s implemented in the sample application. */
sealed interface AppTemplate : Template, AnimationContentKey {
    /** A template that hosts a single model, which should rendered as full-screen element. */
    data class FullScreenTemplate(
        /** The model to be rendered fullscreen. */
        val model: BaseModel,
    ) : AppTemplate {
        override val contentKey: Int
            get() = model.contentKey
    }

    data class HeaderDetailTemplate(
        val header: BaseModel,
        val detail: BaseModel,
    ) : AppTemplate {
        override val contentKey: Int
            // Multiply by 31 to avoid collisions in the sum, e.g. when list changes from 0 to 1 and
            // detail changes from 1 to 0 at the same time.
            get() = header.contentKey * 31 + detail.contentKey
    }
}
