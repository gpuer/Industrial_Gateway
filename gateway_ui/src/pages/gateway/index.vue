<template>
  <div class="app-container">
    <div class="search-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="88px">
      <el-form-item label="网关名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入网关名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button  :icon="Refresh" @click="handleReset">重置</el-button>
        <el-button type="primary" plain :icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>
    </div>

    <el-table :data="gatewayList" style="width: 100%">
      <el-table-column prop="name" label="网关名称" width="180" />
      <el-table-column prop="host" label="网关地址" width="180" />
      <el-table-column prop="port" label="端口号" />
      <el-table-column prop="rack" label="rack" />
      <el-table-column prop="slot" label="slot" />
      <el-table-column prop="slaveId" label="slaveId" />
      <el-table-column prop="type" label="网关类型" />
      <el-table-column width="200" label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-wrapper">
      <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          @change="getList"
        />
    </div>

    <!-- 添加或修改模板对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="vehiclesInfoRef" :model="form" label-width="80px">
        <el-form-item label="网关名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入网关名称" />
        </el-form-item>
        <el-form-item label="网关地址" prop="host">
          <el-input v-model="form.host" placeholder="请输入网关地址" />
        </el-form-item>
        <el-form-item label="端口号" prop="port">
          <el-input v-model="form.port" placeholder="请输入端口号" />
        </el-form-item>
        <el-form-item label="rack" prop="rack">
          <el-input v-model="form.rack" placeholder="请输入rack" />
        </el-form-item>
        <el-form-item label="slot" prop="slot">
          <el-input v-model="form.slot" placeholder="请输入slot" />
        </el-form-item>
          <el-form-item label="slaveId" prop="slaveId">
          <el-input v-model="form.slaveId" placeholder="请输入slaveId" />
        </el-form-item>
        <el-form-item label="网关类型" prop="type">
          <el-input v-model="form.type" placeholder="请输入网关类型" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="reset">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue' 
import { Search, Plus, Refresh } from '@element-plus/icons-vue'
import { listGateway, getGateway, addGateway, updateGateway, delGateway } from "@/api/gateway";
const { proxy } = getCurrentInstance()
const gatewayList = ref([])
const loading = ref(false)
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  name: ""
})
const form = ref({})
const open = ref(false)
const title = ref("")

function handleQuery(){
  queryParams.value.pageNum = 1
  getList()
}

function handleReset(){
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    name: ""
  }
  getList()
}

function getList(){
  loading.value = true
  listGateway(queryParams.value).then(response=>{
    gatewayList.value = response.rows
    total.value = response.total
    loading.value = false
  }) 
}

function reset(){
  form.value = {}
  open.value = false
  title.value = ""
}

function handleUpdate(row){
  reset()
  getGateway(row.id).then(response=>{
    form.value = response.data
    open.value = true
    title.value = "修改"
  })
  
}

function handleAdd(){
  reset()
  open.value = true
  title.value = "添加"
}


function submitForm(){
  console.log(form.value)
  if (form.value.id != null) {
    updateGateway(form.value).then(response=>{
      proxy.$modal.msgSuccess("修改成功")
      open.value = false
      getList()
    })
  } else {
    addGateway(form.value).then(response=>{
      proxy.$modal.msgSuccess("新增成功")
      open.value = false
      getList()
    })
  }
}



function handleDelete(row){
  console.log(row)
  proxy.$modal
    .confirm('是否确认删除"' + row.name + '"的数据项？')
    .then(function () {
      console.log(row.id)
      return delGateway(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {
      console.log("删除失败")
    })
}

getList()

</script>

<style>
.app-container {
  padding: 20px;
}
.search-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
.pagination-wrapper {
  margin-top: 20px; /* 上下间距 */
  display: flex;
  justify-content: center; /* 居中 */
}
</style>
