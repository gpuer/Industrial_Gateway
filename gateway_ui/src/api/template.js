import request from '@/utils/request'


// 查询模板列表
export function listTemplate(query) {
    return request({
      url: "/template/list",
      method: "get",
      params: query,
    })
  }

  // 查询模板组列表
export function groupList() {
    return request({
      url: "/template/groupList",
      method: "get",
    })
  }
  // 查询模板详情
export function getTemplate(id) {
    return request({
      url: "/template/" + id,
      method: "get",
    })
  }

  // 添加模板
export function addTemplate(data) {
    return request({
      url: "/template/add",
      method: "post",
      data: data,
    })
  }

  // 修改模板
export function updateTemplate(data) {
    return request({
      url: "/template/update", 
      method: "post",
      data: data,
    })
  }

  // 删除模板
export function delTemplate(id) {
    return request({
      url: "/template/delete/" + id,
      method: "get",
    })
  }