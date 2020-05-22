package ui.templates

import _self.vcsRoots.*
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell

object BuildAndTestUITemplate : Template({
    name = "Build and Test Fix-it-Friday UI"

    option("shouldFailBuildOnAnyErrorMessage", "true")

    vcs {
        // Treat the UI project as the build root directory
        root(FixItFridayVcs, "+:./fixitfriday.ui => .")
    }

    steps {
        powerShell {
            name = "Install Packages"
            id = "BuildAndTestUITemplate_YarnInstall"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn install
                """.trimIndent()
            }
        }
        powerShell {
            name = "Build"
            id = "BuildAndTestUITemplate_YarnInstall"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn build
                """.trimIndent()
            }
        }
        powerShell {
            name = "Test"
            id = "BuildAndTestUITemplate_YarnInstall"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn test:ci
                """.trimIndent()
            }
        }
    }
})