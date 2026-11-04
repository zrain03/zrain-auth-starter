<#
Fail / File: update-zrain-ui.ps1
Pembangun / Developer: Zrain
Dicipta / Created: 2026-10-27 (bukti: sejarah Git zrain-auth-starter)
Dikemas kini / Last updated: 2026-11-04
BM: Jalankan install, sync dan ujian aset UI daripada root repo dengan rollback manual melalui Git.
EN: Run install, sync and UI asset checks from the repo root, with manual recovery through Git.
#>
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
