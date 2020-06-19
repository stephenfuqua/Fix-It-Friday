package ui.templates

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell

object BuildAndTestUITemplate : Template({
    name = "Build and Test Fix-it-Friday UI"

    option("shouldFailBuildOnAnyErrorMessage", "true")

    vcs {
        // Map the API project as the build root directory.
        root(DslContext.settingsRoot, "+:.")
    }

    steps {
        powerShell {
            name = "Install Packages"
            workingDir = "%project.directory%"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn install
                """.trimIndent()
            }
        }
        powerShell {
            name = "Build"
            workingDir = "%project.directory%"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn build
                """.trimIndent()
            }
        }
        powerShell {
            name = "Test"
            workingDir = "%project.directory%"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn test:ci
                """.trimIndent()
            }
        }
        powerShell {
            name = "Style Check"
            workingDir = "%project.directory%"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn lint:ci
                """.trimIndent()
            }
        }
    }

    features {
        freeDiskSpace {
            id = "jetbrains.agent.free.space"
            requiredSpace = "%build.feature.freeDiskSpace%"
            failBuild = true
        }
    }
})
