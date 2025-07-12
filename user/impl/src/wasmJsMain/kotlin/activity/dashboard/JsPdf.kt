package activity.dashboard

external interface JsPdf {
    fun text(
        text: String,
        x: Int,
        y: Int,
    )

    fun setFontSize(size: Int)

    fun setFont(
        fontName: String,
        fontStyle: String = definedExternally,
    )

    fun addPage()

    fun addImage(
        imageData: String,
        format: String,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    )

    fun save(filename: String)

    val internal: JsPdfInternal
}

external interface JsPdfInternal {
    val pageSize: JsPdfPageSize
}

external interface JsPdfPageSize {
    fun getHeight(): Double

    fun getWidth(): Double
}
