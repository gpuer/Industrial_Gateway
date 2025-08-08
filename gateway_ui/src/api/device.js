import request from '@/utils/request'


// 查询仪表列表
export function listDevice(query) {
    return request({
      url: "/devices/list",
      method: "get",
      params: query,
    })
  }

  // 查询仪表详情
export function getDevice(id) {
  return request({
    url: "/devices/" + id,
    method: "get",
  })
}

// 添加仪表
export function addDevice(data) {
  return request({
    url: "/devices/add",
    method: "post",
    data: data,
  })
}

// 修改仪表
export function updateDevice(data) {
  return request({
    url: "/devices/update", 
    method: "post",
    data: data,
  })
}

// 删除仪表
export function delDevice(id) {
  return request({
    url: "/devices/delete/" + id,
    method: "get",
  })
}