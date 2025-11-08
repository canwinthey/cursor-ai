# PowerShell script to copy project to Jenkins workspace
# Run this script to set up your project in Jenkins workspace

param(
    [string]$JenkinsWorkspacePath = "C:\Program Files\Jenkins\workspace",
    [string]$ProjectName = "student-crud-pipeline",
    [string]$SourcePath = $PSScriptRoot
)

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Jenkins Workspace Setup Script" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Jenkins workspace directory exists
$WorkspaceDir = Join-Path $JenkinsWorkspacePath $ProjectName

if (-not (Test-Path $JenkinsWorkspacePath)) {
    Write-Host "Jenkins workspace directory not found: $JenkinsWorkspacePath" -ForegroundColor Yellow
    Write-Host "Please enter the correct Jenkins workspace path:" -ForegroundColor Yellow
    $JenkinsWorkspacePath = Read-Host "Jenkins Workspace Path"
    $WorkspaceDir = Join-Path $JenkinsWorkspacePath $ProjectName
}

Write-Host "Source Path: $SourcePath" -ForegroundColor Green
Write-Host "Target Path: $WorkspaceDir" -ForegroundColor Green
Write-Host ""

# Create workspace directory if it doesn't exist
if (-not (Test-Path $WorkspaceDir)) {
    Write-Host "Creating workspace directory: $WorkspaceDir" -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $WorkspaceDir -Force | Out-Null
}

# Copy project files
Write-Host "Copying project files..." -ForegroundColor Yellow

# Exclude unnecessary files/directories
$ExcludeItems = @(
    ".git",
    "target",
    ".idea",
    ".vscode",
    "*.iml",
    ".classpath",
    ".project",
    ".settings"
)

# Copy files
Get-ChildItem -Path $SourcePath -Recurse | Where-Object {
    $item = $_
    $shouldExclude = $false
    foreach ($exclude in $ExcludeItems) {
        if ($item.FullName -like "*\$exclude\*" -or $item.Name -like $exclude) {
            $shouldExclude = $true
            break
        }
    }
    return -not $shouldExclude
} | ForEach-Object {
    $relativePath = $_.FullName.Substring($SourcePath.Length + 1)
    $targetPath = Join-Path $WorkspaceDir $relativePath
    $targetDir = Split-Path $targetPath -Parent
    
    if (-not (Test-Path $targetDir)) {
        New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
    }
    
    if (-not $_.PSIsContainer) {
        Copy-Item $_.FullName -Destination $targetPath -Force
    }
}

Write-Host ""
Write-Host "=========================================" -ForegroundColor Green
Write-Host "Setup Complete!" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Project copied to: $WorkspaceDir" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Open Jenkins: http://localhost:8080" -ForegroundColor White
Write-Host "2. Create a new Pipeline job named: $ProjectName" -ForegroundColor White
Write-Host "3. Use 'Pipeline script' and copy the contents of Jenkinsfile" -ForegroundColor White
Write-Host "4. Or use 'Pipeline script from SCM' pointing to: $WorkspaceDir" -ForegroundColor White
Write-Host "5. Click 'Build Now' to run the pipeline" -ForegroundColor White
Write-Host ""

