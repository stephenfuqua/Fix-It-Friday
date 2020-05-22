package ui.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object PullRequestUIBuild : BuildType ({
    templates(ui.templates.BuildAndTestUITemplate)

    triggers {
        vcs {
            id ="vcsTrigger"
            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_CUSTOM
            quietPeriod = 120
            triggerRules = "+:**"
            branchFilter = """
                +:refs/(pull/*)/merge
            """.trimIndent()
        }
    }
})
