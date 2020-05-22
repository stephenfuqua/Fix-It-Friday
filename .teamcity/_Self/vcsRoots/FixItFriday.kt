package _self.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object FixItFridayVcs : GitVcsRoot({
    name = "Fix-It-Friday"
    id("EdFi_AnalyticsTeam_FixItFriday_FixItFriday")
    url = "https://github.com/%github.organization%/Fix-It-Friday"
    branch = "%git.branch.default%"
    branchSpec = """
        refs/heads/(*)
        refs/(pull/*)/merge
    """.trimIndent()
    userNameStyle = GitVcsRoot.UserNameStyle.FULL
    checkoutSubmodules = GitVcsRoot.CheckoutSubmodules.IGNORE
    serverSideAutoCRLF = true
    useMirrors = false
    authMethod = password {
        userName = "%github.username%"
        password = "%github.accessToken%"
    }
})