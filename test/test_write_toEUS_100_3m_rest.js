import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 100,           // 100 clients
  duration: '3m',    // last for 3 mins
};

const address = "Leuven,Tiensestraat12";
const url = `http://dapp25-fx.eastus.cloudapp.azure.com:8082/rest/addOrderAndMeals/${encodeURIComponent(address)}`;

const jsonPayload = JSON.stringify([
  {
    id: "meal-001",
    name: "Pizza",
    description: "Delicious Pizza",
    mealType: "MEAT",
    kcal: 800,
    price: 12.5
  },
  {
    id: "meal-002",
    name: "Salad",
    description: "Fresh Salad",
    mealType: "VEGAN",
    kcal: 300,
    price: 5.5
  }
]);

const headers = {
  'Content-Type': 'application/json'
};

export default function () {
  http.put(url, jsonPayload, { headers });
  sleep(1);
}
