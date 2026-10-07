$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    $jdkLocal = "C:\Program Files\Android\openjdk\jdk-21.0.8"
    if (Test-Path "$jdkLocal\bin\java.exe") {
        $env:JAVA_HOME = $jdkLocal
        $env:Path = "$jdkLocal\bin;$env:Path"
    } else {
        throw "Instale um JDK 21 e configure JAVA_HOME."
    }
}
$maven = Get-Command mvn -ErrorAction SilentlyContinue
if ($maven) {
    & $maven.Source spring-boot:run
} else {
    $mavenLocal = "$env:USERPROFILE\Documents\Codex\tools\apache-maven-3.9.11\bin\mvn.cmd"
    if (-not (Test-Path $mavenLocal)) { throw "Instale o Maven e adicione seu bin ao PATH." }
    & $mavenLocal spring-boot:run
}
exit $LASTEXITCODE
