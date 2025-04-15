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

const jsonPayload = JSON.stringify({
  address: 'leuven',
  meals: ['Steak', 'Portobello', 'Fish and Chips']
});

const headers = {
  'Content-Type': 'application/json'
};

export default function () {
  http.post('http://dapp25-fx.eastus.cloudapp.azure.com:8082/rest/addOrderAndMeals', jsonPayload, { headers });
  sleep(1);
}

