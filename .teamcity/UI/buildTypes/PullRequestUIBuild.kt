package ui.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object PullRequestUIBuild : BuildType ({
    name = "Pull Request Build and Test"
    templates(_self.templates.PullRequestTemplate)

    params{
        param("vcs.exclude.directories", """
            -:.teamcity
            -:fixitfriday.api
            -:edfi.fif.database
        """.trimIndent())
    }
})
