package _self

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project

object FixItFridayProject : Project({
    description = "Projects Owned by the Analytics Team"

    params {
        param("build.feature.freeDiskSpace", "2gb")
        param("git.branch.default", "development")
        param("git.branch.specification", """
            refs/heads/(*)
            refs/(pull/*)/head
        """.trimIndent())
        param("octopus.deploy.timeout", "00:45:00")
        param("octopus.release.environment", "Integration")
        param("vcs.checkout.rules","""
            +:.teamcity => .teamcity
            +:%project.directory% => %project.directory%
            +:LICENSE =>
            +:NOTICES.md =>
        """.trimIndent())
    }

    subProject(ui.UIProject)
    subProject(api.APIProject)

    template(_self.templates.BuildAndTestTemplate)
    template(_self.templates.PullRequestTemplate)

})
