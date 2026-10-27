# Update the Zrain Auth Starter *website* from the official pinned
# @zrain/ui GitHub dependency. Maven/JWT backend remains independent.
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
  npm install
  if ($LASTEXITCODE -ne 0) { throw 'GitHub Zrain UI dependency installation failed.' }
  npm run ui:sync
  if ($LASTEXITCODE -ne 0) { throw 'Zrain UI synchronization failed.' }
  npm run test:ui
  if ($LASTEXITCODE -ne 0) { throw 'Auth Starter website UI tests failed.' }
  Write-Host 'Auth Starter shared UI is synchronized and tested.'
  Write-Host 'Review changes, then commit and push to main to publish through your configured GitHub Pages.'
} finally {
  Pop-Location
}
