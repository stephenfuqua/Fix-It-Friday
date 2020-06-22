package api.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object PullRequestAPIBuild : BuildType ({
    name = "Pull Request Build and Test"
    templates(_self.templates.PullRequestTemplate)

    params{
        param("vcs.exclude.directories", """
            -:.teamcity
            -:fixitfriday.ui
            -:edfi.fif.database
        """.trimIndent())
    }
})
