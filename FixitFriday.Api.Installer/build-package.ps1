#requires -version 5

param(
    [string]
    [validateset("debug", "release")]
    $Configuration = "debug",

    [string]
    [Parameter(Mandatory=$true)]
    $Version,

    [string]
    $VersionSuffix = "",

    [string]
    $AppCommonPackageVersion = "1.0.0-pre1055"
)

$ErrorActionPreference = "Stop"

function Invoke-DotnetPack {

    if ($VersionSuffix) {
        $Version += "-$VersionSuffix"
    }

    $parameters = @(
        "pack", "../FixItFriday.Api/FixItFriday.Api.csproj",
        "-p:PackageVersion=$Version"
        "-p:NuspecFile=$(Resolve-Path "$PSScriptRoot/FixItFriday.Api.Installer.nuspec")",
        "-p:NuspecProperties=\""Configuration=$Configuration;Version=$Version\""",
        "--no-build",
        "--no-restore",
        "--output", "$PSScriptRoot/dist",
        # Suppress warnings about script files not being recognized and executed
        "-nowarn:NU5111,NU5110"
    )

    write-host @parameters

    dotnet @parameters
}

function New-EdFiDirectories {
    # This is a hack for TeamCity - create empty ODS and Implementation directories so that
    # the path resolver will be satified. When run locally this should have no impact.
    New-Item -ItemType Directory -Path "$edFiRepoContainer/Ed-Fi-ODS" -Force | Out-Null
    New-Item -ItemType Directory -Path "$edFiRepoContainer/Ed-Fi-ODS-Implementation" -Force | Out-Null
}

function Invoke-DownloadAppCommon {
    # Download App Common
    $parameters = @{
        PackageName = "EdFi.Installer.AppCommon"
        PackageVersion = $AppCommonPackageVersion
        ToolsPath = "../tools"
    }
    
    Get-NugetPackage @parameters
}

function Copy-AppCommonFilesIntoEdFiDirectories {
    param (
        [string]
        $AppCommonDirectory
    )
    
    # Copy Ed-Fi-XYZ folders from App Common folder to current 
    @(
        "Ed-Fi-Common"
        "Ed-Fi-ODS"
        "Ed-Fi-ODS-Implementation"
    ) | ForEach-Object {
        Copy-Item -Recurse -Path $appCommonDirectory/$_ -Destination $PsScriptRoot -Force
    }

    # Move AppCommon's modules into Ed-Fi-Common so that they are discoverable with pathresolver
    @(
        "Application"
        "Environment"
        "IIS"
    ) | ForEach-Object {
        $parameters = @{
            Recurse = $true
            Force = $true
            Path = "$appCommonDirectory/$_"
            Destination = "$PSScriptRoot/Ed-Fi-Common/logistics/scripts/modules"
        }
        Copy-Item @parameters
    }
}

$env:PathResolverRepositoryOverride = "Ed-Fi-Common;Ed-Fi-Ods;Ed-Fi-ODS-Implementation;"
Import-Module -Force -Scope Global "$PsScriptRoot/../../Ed-Fi-ODS-Implementation/logistics/scripts/modules/path-resolver.psm1"
Import-Module -Force $folders.modules.invoke("packaging/nuget-helper.psm1")

New-EdFiDirectories
$appCommonDirectory = Invoke-DownloadAppCommon
Copy-AppCommonFilesIntoEdFiDirectories $appCommonDirectory

Invoke-DotnetPack