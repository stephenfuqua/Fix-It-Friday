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
    "edfi.buzz.api.nuspec",
    "-version",
    $FullVersion
  )

  Write-Host "Executing: nuget.exe" @parameters -ForegroundColor Magenta
  &nuget.exe @parameters
}

function Invoke-PrepForDistribution {
  # The normal `nest build` output does not include the node modules required
  # for distribution. Convert yarn.lock to package-lock.json and copy into the
  # dist directory.
  Push-Location "$PSScriptRoot/../"
  Write-Host "Executing: yarn synp -s ./yarn.lock" -ForegroundColor Magenta
  &yarn synp -s .\yarn.lock

  if ($LASTEXITCODE -ne 0) {
    Write-Error "Lock file conversion failed."
    Pop-Location
    Exit
  }

  Move-Item -Path "package-lock.json" -Destination "./dist" -Force

  # Also need a copy of package.json
  Copy-Item -Path "package.json" -Destination "./dist" -Force

  # Copy the *.graphql files, which are not included in the dist output
  Copy-Item -Path "./src/graphql/schema" -Destination "./dist/graphql" -Force -Recurse
  Pop-Location
}

if (-not (Test-Path "$PSScriptRoot/../dist")) {
  Write-Error "Run `yarn build` before calling this script"
  Exit
}

Invoke-PrepForDistribution

$version = Read-VersionNumberFromPackageJson
Invoke-NuGetPack -FullVersion "$version-pre$($BuildCounter.PadLeft(4,'0'))"
Invoke-NuGetPack -FullVersion $version
