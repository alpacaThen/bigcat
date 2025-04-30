<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET');

// Check if docker command is available
if (!shell_exec('which docker')) {
    http_response_code(500);
    echo json_encode(['error' => 'Docker is not installed or not accessible']);
    exit;
}

function getDockerServices() {
    $output = shell_exec('docker ps --format "{{.Names}}|{{.Ports}}|{{.Status}}|{{.Image}}"');
    $services = [];
    
    if ($output) {
        $lines = explode("\n", trim($output));
        foreach ($lines as $line) {
            if (empty($line)) continue;
            
            list($name, $ports, $status, $image) = explode('|', $line);
            
            // Extract port number if available
            preg_match('/(\d+)->\d+\/tcp/', $ports, $matches);
            $port = $matches[1] ?? '';
            
            $services[] = [
                'name' => $name,
                'port' => $port,
                'status' => strpos($status, 'Up') !== false ? 'running' : 'stopped',
                'image' => $image,
                'uptime' => strpos($status, 'Up') !== false ? extractUptime($status) : 'N/A'
            ];
        }
    }
    
    return $services;
}

function extractUptime($status) {
    if (preg_match('/Up\s+([^,]+)/', $status, $matches)) {
        return $matches[1];
    }
    return 'N/A';
}

try {
    $services = getDockerServices();
    if (empty($services)) {
        echo json_encode([
            'message' => 'No Docker containers are currently running',
            'services' => []
        ]);
    } else {
        echo json_encode([
            'message' => 'Successfully retrieved Docker services',
            'services' => $services
        ]);
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Failed to get Docker services: ' . $e->getMessage()]);
}
?> 