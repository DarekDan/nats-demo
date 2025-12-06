import http from 'k6/http';
import {check, sleep} from 'k6';

// Test configuration
export const options = {
    scenarios: {
        spike_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                {duration: '30s', target: 50},
                {duration: '1m', target: 100},
                {duration: '10s', target: 200}, // spike!
                {duration: '1m', target: 100},
                {duration: '30s', target: 0},
            ],
        },
    },
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<200'],
    },
};

// Helper function to generate random string
function randomString(length) {
    const charset = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let res = '';
    for (let i = 0; i < length; i++) {
        res += charset.charAt(Math.floor(Math.random() * charset.length));
    }
    return res;
}

// Helper function to generate random number
function randomAmount(min, max) {
    return (Math.random() * (max - min) + min).toFixed(2);
}

export default function () {
    const url = 'http://localhost:8080/api/orders/simple';

    // Randomize payload data
    const payload = JSON.stringify({
        orderId: `ORD-${randomString(6)}`,
        customerId: `CUST-${randomString(4)}`,
        amount: parseFloat(randomAmount(10, 500)),
        status: 'PENDING',
        orderDateTime: new Date().toISOString()
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    // validation
    check(res, {
        'is status 200 or 201': (r) => r.status === 200 || r.status === 201,
    });

    sleep(1);
}
