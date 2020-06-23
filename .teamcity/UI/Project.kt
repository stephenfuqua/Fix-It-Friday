// SPDX-License-Identifier: Apache-2.0
// Licensed to the Ed-Fi Alliance under one or more agreements.
// The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
// See the LICENSE and NOTICES files in the project root for more information.

package ui

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object UIProject : Project({
    id("FixItFriday_UI")
    name = "UI"
    description = "Fix-it-Friday User Interface"

    buildType(ui.buildTypes.PullRequestUIBuild)
    buildType(ui.buildTypes.BranchUIBuild)
    buildType(ui.buildTypes.DeployUIBuild)

    params{
        param("project.directory", "./fixitfriday.ui");
        param("octopus.release.version","<placeholder value>")
        param("octopus.release.project", "Fix-it-Friday UI")
        param("octopus.project.id", "Projects-112")
        // Include the root - giving us license, notices.md, and .teamcity.
        // Then exclude the other projects.
        param("vcs.checkout.rules","""
            +:. =>
            -:edfi.fif.api
            -:edfi.fif.database
            -:edfi.fif.api.netcore
            -:edfi.fixitfriday.installer
            -:fixitfriday.api
        """.trimIndent())
    }
})
