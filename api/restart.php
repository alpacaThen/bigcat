<?php
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['error' => 'Method not allowed']);
    exit;
}

$serviceName = $_GET['service'] ?? '';
if (empty($serviceName)) {
    http_response_code(400);
    echo json_encode(['error' => 'Service name is required']);
    exit;
}

// Sanitize service name to prevent command injection
$serviceName = escapeshellarg($serviceName);

// Restart the service
$command = "docker restart $serviceName";
$output = shell_exec($command);

if ($output === null) {
    http_response_code(500);
    echo json_encode(['error' => 'Failed to restart service']);
    exit;
}

echo json_encode(['success' => true, 'message' => 'Service restarted successfully']);
?> 