<?php
header('Content-Type: application/json');

function getDockerServices() {
    $output = shell_exec('docker ps --format "{{.Names}}|{{.Ports}}|{{.Status}}"');
    $services = [];
    
    if ($output) {
        $lines = explode("\n", trim($output));
        foreach ($lines as $line) {
            if (empty($line)) continue;
            
            list($name, $ports, $status) = explode('|', $line);
            
            // Extract port number if available
            preg_match('/(\d+)->\d+\/tcp/', $ports, $matches);
            $port = $matches[1] ?? '';
            
            $services[] = [
                'name' => $name,
                'port' => $port,
                'status' => strpos($status, 'Up') !== false ? 'running' : 'stopped'
            ];
        }
    }
    
    return $services;
}

echo json_encode(getDockerServices());
?> 