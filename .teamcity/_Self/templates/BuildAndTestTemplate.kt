package _self.templates

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object BuildAndTestTemplate : BuildAndTestBaseClass() {
    init {
        name = "Build and Test Node.js Template"
        id = RelativeId("BuildAndTestTemplate")

        artifactRules = "+:%project.directory%/eng/*.nupkg"

        steps {
            // Additional packaging step to augment the template build
            powerShell {
                name = "Package"
                id = "BranchUIBuild_Package"
                workingDir = "%project.directory%/eng"
                formatStderrAsError = true
                scriptMode = script {
                    content = """
                        .\build-package.ps1 -BuildCounter %build.counter%
                    """.trimIndent()
                }
            }
        }

        triggers {
            vcs {
                id ="vcsTrigger"
                quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_CUSTOM
                quietPeriod = 120
                branchFilter = "+:<default>"
            }
        }
    }
}
