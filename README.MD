# NATS.io with Spring Boot

## Inspirations

[Spring Boot 4 JMS Client](https://youtu.be/91xVrWlzIe4?si=h89GtfC8Jg650eOe)

## Test Environment

* JDK 25
* AMD Ryzen 9 7945HX

```text

         /\      Grafana   /‾‾/                                                                                                                                                                                                    
    /\  /  \     |\  __   /  /                                                                                                                                                                                                     
   /  \/    \    | |/ /  /   ‾‾\                                                                                                                                                                                                   
  /          \   |   (  |  (‾)  |                                                                                                                                                                                                  
 / __________ \  |_|\_\  \_____/ 

     execution: local
        script: order_creation_performance_test.js
        output: -

     scenarios: (100.00%) 1 scenario, 200 max VUs, 3m40s max duration (incl. graceful stop):
              * spike_test: Up to 200 looping VUs for 3m10s over 5 stages (gracefulRampDown: 30s, gracefulStop: 30s)



  █ THRESHOLDS 

    http_req_duration
    ✓ 'p(95)<200' p(95)=2.81ms

    http_req_failed
    ✓ 'rate<0.01' rate=0.00%


  █ TOTAL RESULTS 

    checks_total.......................: 168501  886.47658/s
    checks_succeeded...................: 100.00% 168501 out of 168501
    checks_failed......................: 0.00%   0 out of 168501

    ✓ is status 200 or 201

    HTTP
    http_req_duration.......................................................: avg=1.38ms   min=0s    med=1.04ms   max=832.85ms p(90)=2.14ms   p(95)=2.81ms  
      { expected_response:true }............................................: avg=1.38ms   min=0s    med=1.04ms   max=832.85ms p(90)=2.14ms   p(95)=2.81ms  
    http_req_failed.........................................................: 0.00%  0 out of 168501
    http_reqs...............................................................: 168501 886.47658/s

    EXECUTION
    iteration_duration......................................................: avg=102.06ms min=100ms med=101.42ms max=941.21ms p(90)=102.88ms p(95)=103.67ms
    iterations..............................................................: 168501 886.47658/s
    vus.....................................................................: 1      min=1           max=199
    vus_max.................................................................: 200    min=200         max=200

    NETWORK
    data_received...........................................................: 25 MB  132 kB/s
    data_sent...............................................................: 45 MB  237 kB/s




running (3m10.1s), 000/200 VUs, 168501 complete and 0 interrupted iterations
spike_test ✓ [======================================] 000/200 VUs  3m10s
```
