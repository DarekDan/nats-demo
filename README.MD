# Test Results

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
    ✓ 'p(95)<200' p(95)=1.22ms

    http_req_failed
    ✓ 'rate<0.01' rate=0.00%


  █ TOTAL RESULTS 

    checks_total.......................: 17327   90.794105/s
    checks_succeeded...................: 100.00% 17327 out of 17327
    checks_failed......................: 0.00%   0 out of 17327

    ✓ is status 200 or 201

    HTTP
    http_req_duration.......................................................: avg=736.44µs min=0s med=546.79µs max=104.31ms p(90)=1.11ms p(95)=1.22ms
      { expected_response:true }............................................: avg=736.44µs min=0s med=546.79µs max=104.31ms p(90)=1.11ms p(95)=1.22ms
    http_req_failed.........................................................: 0.00%  0 out of 17327
    http_reqs...............................................................: 17327  90.794105/s

    EXECUTION
    iteration_duration......................................................: avg=1s       min=1s med=1s       max=1.1s     p(90)=1s     p(95)=1s    
    iterations..............................................................: 17327  90.794105/s
    vus.....................................................................: 3      min=1          max=200
    vus_max.................................................................: 200    min=200        max=200

    NETWORK
    data_received...........................................................: 2.6 MB 14 kB/s
    data_sent...............................................................: 4.6 MB 24 kB/s




running (3m10.8s), 000/200 VUs, 17327 complete and 0 interrupted iterations
spike_test ✓ [======================================] 000/200 VUs  3m10s
```
