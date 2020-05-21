package _Self

import _Self.vcsRoots.*
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project

object Project : Project({
    description = "Projects Owned by the ODS Platform Team"

    vcsRoot(FixItFriday)

    params {
        param("git.branch.default", "development")
        param("git.branch.specification", """
            refs/heads/(*)
            refs/(pull/*)/merge
        """.trimIndent())
        param("version.major", "0")
        param("version.minor", "1")
        param("version.patch", "0")
        param("version.core", "%version.major%.%version.minor%.%version.patch%")
        param("version.prerelease", "pre%build.counter%")
        param("version", "%version.core%-%version.prerelease%")
    }

    subProject(API.Project)
    subProject(UI.Project)
})
