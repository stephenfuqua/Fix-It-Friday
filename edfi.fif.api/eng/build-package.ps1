# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

#requires -version 5

[CmdletBinding()]
param(
    [string]
    $BuildCounter = 0
)

function Read-VersionNumberFromPackageJson {
  $packageJson = Get-Content -Path "$PsScriptRoot/../package.json" | ConvertFrom-Json
  return $packageJson.version
}

function Invoke-NuGetPack{
  param(
    [string]
    [Parameter(Mandatory=$true)]
    $FullVersion
  )

  $parameters = @(
    "pack",
    "edfi.fif.api.nuspec",
    "-version",
    $FullVersion
  )

  Write-Host "Executing: &nuget.exe" @parameters -ForegroundColor Magenta
  &nuget.exe @parameters
}

function Invoke-PrepForDistribution {
  # The normal `nest build` output does not include the node modules required
  # for distribution. Copy the required files to the dist directory and run
  # `yarn install --prod` to load only the required modules.
  $dist = "$PSScriptRoot/../dist"
  Copy-Item -Path "$PSScriptRoot/../package.json" -Destination $dist -Force
  Copy-Item -Path "$PSScriptRoot/../yarn.lock" -Destination $dist -Force

  Push-Location $dist
  Write-Host "Executing: &yarn install --prod" -ForegroundColor Magenta
  &yarn install --prod
  Pop-Location

  if ($LASTEXITCODE -ne 0) {
    Write-Error "Yarn install failed."
    Exit
  }

  # Also need to copy the *.graphql files, which are not included in the
  # dist output from `yarn build`.
  Copy-Item -Path "$PSScriptRoot/../src/graphql/schema" -Destination "$dist/graphql/schema"
}

Invoke-PrepForDistribution

$version = Read-VersionNumberFromPackageJson
Invoke-NuGetPack -FullVersion "$version-pre$($BuildCounter.PadLeft(4,'0'))"
Invoke-NuGetPack -FullVersion $version
