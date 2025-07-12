@file:Suppress("ktlint:standard:max-line-length")

package activity.dashboard

object AboutText {
    val howItWasMade =
        """
        This application was created by a team of developers passionate about health and fitness. Our goal was to provide users with a seamless way to export their Fitbit activity data into a convenient PDF format. Built using modern web technologies, the app ensures a responsive and user-friendly experience across a wide range of devices.
        """.trimIndent()

    val architecture =
        """
        The application is built using Kotlin Multiplatform with a WebAssembly frontend, delivering a fast and responsive dashboard experience directly in the browser. It uses OAuth 2.0 with PKCE for secure client-side authentication with Fitbit, removing the need to store client secrets. A lightweight Node.js server hosted on a VPS serves as a proxy to work around CORS limitations and facilitate communication with the Fitbit API. This architecture prioritizes performance, security, and user privacy throughout the entire flow.
        """.trimIndent()

    val privacyPolicy =
        """
        We take your privacy seriously. This application does not store any personal data on our servers. All data processing and PDF generation occur in real-time within your browser, and no user data is retained after the process is complete. We adhere to strong security practices to protect the confidentiality and integrity of your Fitbit data.
        """.trimIndent()

    val contact =
        """
        If you have any questions, feedback, or need support, please don’t hesitate to contact us at patchroot.dev@gmail.com. We’re committed to continuously improving the application and greatly appreciate your input.
        """.trimIndent()
}
