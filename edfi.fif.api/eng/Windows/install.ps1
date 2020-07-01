# SPDX-License-Identifier: Apache-2.0
# Licensed to the Ed-Fi Alliance under one or more agreements.
# The Ed-Fi Alliance licenses this file to you under the Apache License, Version 2.0.
# See the LICENSE and NOTICES files in the project root for more information.

#requires -version 5

[CmdletBinding()]
param(
  [string]
  $InstallPath = "c:\Ed-Fi\Fix-it-Friday\API",

  [string]
  $WinSWUrl = "https://github.com/winsw/winsw/releases/download/v2.9.0/WinSW.NETCore31.zip"
)

# TODO: insure this is run as an administrator

# TODO: refactor to share functions instead of duplicate them
# in various application installer scripts.
function Get-FileNameWithoutExtensionFromUrl {
  param(
    [string]
    $Url
  )

  if (($Url -match "([a-zA-Z0-9\.\-]+)\.zip")) {
    return $Matches[1];
  }

  throw "Unable to parse file name from $Url"
}

function Get-HelperAppIfNotExists {
  param(
    [string]
    $Url
  )
  $version = Get-FileNameWithoutExtensionFromUrl -Url $Url

  if (-not (Test-Path -Path "$InstallPath\$version")) {
    $outFile = ".\$version.zip"
    Invoke-WebRequest -Uri $Url -OutFile $outFile

    Expand-Archive -Path $outFile

    if ((Test-Path -Path "$version\$version")) {
      Move-Item -Path "$version\$version" -Destination $InstallPath
      Remove-Item -Path $version
    }
    else {
      Move-Item -Path "$version" -Destination $InstallPath
    }
  }

  return $version
}

function Install-WebApplication {
  $parameters = @{
    Path = "$PSScriptRoot\..\dist"
    Destination = "$InstallPath\dist"
    Recurse = $true
    Force = $true
  }
  Copy-Item @parameters
}

function Install-NodeService {
  param(
    [string]
    $winSwVersion
  )

  $serviceName = "edfi-fif-api"

  # If service already exists, stop and remove it so that new settings
  # will be applied.
  $exists = Get-Service -name $serviceName -ErrorAction SilentlyContinue

  if ($exists) {
    Stop-Service -name $serviceName
    &sc.exe delete $serviceName
  }

  # Rename WindowsService.exe to FixItFridayApi.exe
  $winServiceExe = "$InstallPath\$winSwVersion\WindowsService.exe"
  $fixItFridayApiExe = "$InstallPath\$winSwVersion\FixItFridayApi.exe"
  if ((Test-Path -Path "$InstallPath\$winSwVersion\WindowsService.exe")) {
    Move-Item -Path $winServiceExe -Destination $fixItFridayApiExe
  }

  # Copy the config XML file to install directory
  $xmlFile = "$InstallPath\$winSwVersion\FixItFridayApi.xml"
  Copy-Item -Path "$PSScriptRoot\FixItFridayApi.xml" -Destination $xmlFile -Force

  # Inject the correct path to nginx.exe into the config XML file
  $content = Get-Content -Path $xmlFile -Encoding UTF8
  $content = $content.Replace("{0}", "$InstallPath")
  $content | Out-File -FilePath $xmlFile -Encoding UTF8 -Force

  # Create and start the service
  &$fixItFridayApiExe install
  &$fixItFridayApiExe start
}

Write-Host "Begin Fix-it-Friday API installation..." -ForegroundColor Yellow

New-Item -Path $InstallPath -ItemType Directory -Force | Out-Null

Install-WebApplication

$winSwVersion = Get-HelperAppIfNotExists -Url $WinSWUrl
Install-NodeService -winSwVersion $winSwVersion

Write-Host "End Fix-it-Friday API installation." -ForegroundColor Yellow
