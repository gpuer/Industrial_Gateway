import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const service = axios.create({
  baseURL: 'http://localhost:8083', // 根据你的接口地址调整
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
  }
})

// 请求拦截器
service.interceptors.request.use(config => {
  // 自动拼接 GET 参数
  if (config.method === 'get' && config.params) {
    config.url += '?' + new URLSearchParams(config.params).toString()
    config.params = {}
  }
  return config
}, error => {
  return Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(response => {
  const res = response.data
  const code = res.code || 200

  if (code !== 200) {
    ElMessage.error(res.msg || '请求出错')
    return Promise.reject(new Error(res.msg || 'Error'))
  }
  return res
}, error => {
  let message = error.message
  if (message.includes('timeout')) {
    message = '请求超时'
  } else if (message.includes('Network Error')) {
    message = '网络异常，无法连接服务器'
  } else if (message.includes('status code')) {
    const status = message.slice(-3)
    message = `接口异常：状态码 ${status}`
  }
  ElMessage.error(message)
  return Promise.reject(error)
})

export default service