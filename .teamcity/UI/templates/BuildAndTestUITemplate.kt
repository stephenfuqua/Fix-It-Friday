package ui.templates

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell

object BuildAndTestUITemplate : Template({
    name = "Build and Test Fix-it-Friday UI"

    option("shouldFailBuildOnAnyErrorMessage", "true")

    vcs {
        // To avoid duplicate VCS roots, we don't redefine the vcsroot here in
        // source code. We can access it through "DslContext.settingsRoot".
        
        // Map the UI project as the build root directory.
        root(DslContext.settingsRoot, "+:./fixitfriday.ui => .")
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
            id = "BuildAndTestUITemplate_YarnBuild"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn build
                """.trimIndent()
            }
        }
        powerShell {
            name = "Test"
            id = "BuildAndTestUITemplate_YarnTest"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                    yarn test:ci
                """.trimIndent()
            }
        }
    }

    features {
        feature {
            type = "xml-report-plugin"
            param("xmlReportParsing.reportType", "junit")
            param("xmlReportParsing.reportDirs", "+:junit.xml")
        }
        // For future reference, once linting is built-in. Probably wrong file name.
        // feature {
        //     type = "xml-report-plugin"
        //     param("xmlReportParsing.reportType", "jslint")
        //     param("xmlReportParsing.reportDirs", "+:lint.xml")
        // }
    }
})