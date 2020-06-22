package api.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object BranchAPIBuild : BuildType ({
    name = "Branch Build and Test"
    templates(_self.templates.BuildAndTestUITemplate)

    artifactRules = "+:%project.directory%/eng/*.nupkg"

    features {
        // Default setting is to clean before next build
        swabra {
        }
    }

    steps {
        // Additional packaging step to augment the template build
        powerShell {
            name = "Package"
            id = "Package"
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
            triggerRules = "+:**"
            branchFilter = "+:<default>"
        }
    }
})
