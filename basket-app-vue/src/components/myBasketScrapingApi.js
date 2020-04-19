import axios from 'axios'
export default axios.create({
  baseURL: 'http://192.168.100.31:8081',
  timeout: 5000,
  headers: {
    'X-Auth-Token': 'f2b6637ddf355a476918940289c0be016a4fe99e3b69c83d',
    'Content-Type': 'application/json'
  }
})
