import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  stages: [
    { duration: '1m', target: 10 },
    { duration: '1m', target: 30 },
    { duration: '1m', target: 60 },
    { duration: '1m', target: 80 },
    { duration: '1m', target: 100 },
  ],
};

export default function () {
  http.get('dsgt2025westeu.westeurope.cloudapp.azure.com:3020/rest/meals');
  sleep(1);
}

