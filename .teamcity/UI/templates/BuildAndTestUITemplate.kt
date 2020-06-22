package ui.templates

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell

object BuildAndTestUITemplate : BuildAndTestBaseClass() {
    init {
        name = "Build and Test Fix-it-Friday UI"
    }
}
