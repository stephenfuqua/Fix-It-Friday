package ui.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object BranchUIBuild : BuildType ({
    name = "Branch Build and Test"
    templates(_self.templates.BuildAndTestTemplate)
})
