# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

#requires -version 5

[CmdletBinding()]
param(
    [string]
    [Parameter(Mandatory=$true)]
    $Version,

    [string]
    $BuildCounter = 0
)

$ErrorActionPreference = "Stop"

function Invoke-DotnetPack {
    param (
        [string]
        [Parameter(Mandatory=$true)]
        $PackageVersion
    )

    $parameters = @(
        # CSPROJ file is basically irrelevant here, but required for the command
        "pack", "../FixItFriday.Api/FixItFriday.Api.csproj",
        "-p:PackageVersion=$Version"
        "-p:NuspecFile=$(Resolve-Path "$PSScriptRoot/EdFi.FixItFriday.UI.nuspec")",
        "-p:NuspecProperties=\""Configuration=Release;Version=$PackageVersion\""",
        "--no-build",
        "--output", "$PSScriptRoot/dist",
        # Suppress warnings about script files not being recognized and executed
        "-nowarn:NU5111,NU5110,NU5100"
    )

    Write-Host "dotnet $parameters"  -ForegroundColor Magenta
    &dotnet @parameters
}

Invoke-DotnetPack -PackageVersion "$Version-pre$($BuildCounter.PadLeft(4,'0'))"
Invoke-DotnetPack -PackageVersion "$Version"
