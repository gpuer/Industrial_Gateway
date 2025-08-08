import request from '@/utils/request'


 // 查询网关列表
export function listGateway(query) {
    return request({
      url: "/gateway/list",
      method: "get",
      params: query,
    })
  }

  // 查询所有网关
export function allGateway() {
  return request({
    url: "/gateway/all",
    method: "get",
  })
}
  // 查询网关详情
export function getGateway(id) {
  return request({
    url: "/gateway/" + id,
    method: "get",
  })
}

// 添加网关
export function addGateway(data) {
  return request({
    url: "/gateway/add",
    method: "post",
    data: data,
  })
}

// 修改网关
export function updateGateway(data) {
  return request({
    url: "/gateway/update", 
    method: "post",
    data: data,
  })
}

// 删除网关
export function delGateway(id) {
  return request({
    url: "/gateway/delete/" + id,
    method: "get",
  })
}