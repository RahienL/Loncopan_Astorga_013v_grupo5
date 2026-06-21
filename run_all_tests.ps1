$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$servicios = @('ms-usuarios', 'ms-catalogo', 'ms-ordenes', 'ms-inventario', 'ms-pagos', 'ms-notificaciones', 'ms-envios', 'ms-resenas', 'ms-recomendaciones', 'ms-reportes')
$resultado = @()
$fecha = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

Write-Host "============================================" 
Write-Host "EJECUCION DE TESTS MOCKITO - BOOKHUB"
Write-Host "Fecha: $fecha"
Write-Host "============================================"
Write-Host ""

$passou = 0
$fallaron = 0

foreach ($svc in $servicios) {
    Write-Host "Ejecutando $svc..." -NoNewline
    cd "$root\$svc"
    $output = docker run --rm -v "${root}:/workspace" -w /workspace/$svc maven:3.9.9-eclipse-temurin-21 mvn test 2>&1
    
    if ($output -like "*BUILD SUCCESS*") {
        Write-Host " [OK]" -ForegroundColor Green
        $resultado += "$svc | OK | ExitCode: 0"
        $passou++
    } else {
        Write-Host " [FALLO]" -ForegroundColor Red
        $resultado += "$svc | FALLO | ExitCode: 1"
        $fallaron++
    }
}

Write-Host ""
Write-Host "============================================"
Write-Host "RESULTADO CONSOLIDADO"
Write-Host "============================================"

foreach ($r in $resultado) {
    Write-Host $r
}

Write-Host ""
Write-Host "RESUMEN: $passou OK - $fallaron FALLO"

if ($fallaron -eq 0) {
    Write-Host "ESTADO GLOBAL: OK - TODO PASO SIN FALLOS" -ForegroundColor Green
} else {
    Write-Host "ESTADO GLOBAL: FALLO - REVISAR SERVICIOS" -ForegroundColor Red
}

$resultado | Out-File -FilePath "$root\evidencias\mockito\resultado-final.txt" -Encoding UTF8
Add-Content -Path "$root\evidencias\mockito\resultado-final.txt" -Value ""
Add-Content -Path "$root\evidencias\mockito\resultado-final.txt" -Value "RESUMEN: $passou OK - $fallaron FALLO"
if ($fallaron -eq 0) {
    Add-Content -Path "$root\evidencias\mockito\resultado-final.txt" -Value "ESTADO GLOBAL: OK - TODO PASO SIN FALLOS"
} else {
    Add-Content -Path "$root\evidencias\mockito\resultado-final.txt" -Value "ESTADO GLOBAL: FALLO - REVISAR SERVICIOS"
}
