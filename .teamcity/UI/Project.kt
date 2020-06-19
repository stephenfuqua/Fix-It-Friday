package ui

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project

object UIProject : Project({
    id("FixItFriday_UI")
    name = "UI"
    description = "Fix-it-Friday User Interface"

    buildType(ui.buildTypes.PullRequestUIBuild)
    buildType(ui.buildTypes.BranchUIBuild)
    buildType(ui.buildTypes.DeployUIBuild)

    template(ui.templates.BuildAndTestUITemplate)

    params{
        param("project.directory", "./fixitfriday.ui");
        param("octopus.release.version","<placeholder value>")
        param("octopus.release.project", "Fix-it-Friday UI")
        param("octopus.project.id", "Projects-112")
    }
})
