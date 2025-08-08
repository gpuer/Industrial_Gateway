<template>
  <div class="app-container">
    <div class="search-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="88px">
      <el-form-item label="参数名称" prop="fieldName">
        <el-input v-model="queryParams.fieldName" placeholder="请输入参数名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="所属模板" prop="templateName">
        <el-select v-model="queryParams.templateName" v-model:value="queryParams.templateName" style="width: 220px" placeholder="请选择所属模板">
          <el-option v-for="item in templateGroupList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button  :icon="Refresh" @click="handleReset">重置</el-button>
        <el-button type="primary" plain :icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>
    </div>

    <el-table :data="templateList" style="width: 100%">
      <el-table-column prop="fieldName" label="参数名称"/>
      <el-table-column prop="type" label="数据类型"/>
      <el-table-column prop="length" label="寄存器长度" />
      <el-table-column prop="offset" label="相对偏移" />
      <el-table-column prop="scale" label="调整系数" />
      <el-table-column prop="wordSwap" label="wordSwap" />
      <el-table-column prop="byteSwap" label="byteSwap" />
      <el-table-column prop="templateName" label="所属模板" />
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
        <el-form-item label="参数名称" prop="fieldName">
          <el-input v-model="form.fieldName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="数据类型" prop="type">
          <el-input v-model="form.type" placeholder="请输入数据类型" />
        </el-form-item>
        <el-form-item label="寄存器长度" prop="length">
          <el-input v-model="form.length" placeholder="请输入寄存器长度" />
        </el-form-item>
        <el-form-item label="相对偏移" prop="offset">
          <el-input v-model="form.offset" placeholder="请输入相对偏移" />
        </el-form-item>
        <el-form-item label="调整系数" prop="scale">
          <el-input v-model="form.scale" placeholder="请输入调整系数" />
        </el-form-item>
        <el-form-item label="wordSwap" prop="wordSwap">
          <el-input v-model="form.wordSwap" placeholder="请输入wordSwap" />
        </el-form-item>
        <el-form-item label="byteSwap" prop="byteSwap">
          <el-input v-model="form.byteSwap" placeholder="请输入byteSwap" />
        </el-form-item>
        <el-form-item label="所属模板" prop="templateName">
          <el-input v-model="form.templateName" placeholder="请输入所属模板" />
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
import { listTemplate, getTemplate, addTemplate, updateTemplate, delTemplate,groupList } from "@/api/template";
const { proxy } = getCurrentInstance()
const templateList = ref([])
const templateGroupList = ref([])
const loading = ref(false)
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  templateName: ""
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
    fieldName: "",
    templateName: ""
  }
  getList()
}

function getList(){
  loading.value = true
  listTemplate(queryParams.value).then(response=>{
    templateList.value = response.rows
    total.value = response.total
    loading.value = false
  }) 
}

function getTemplateGroupList(){
  groupList().then(response=>{
    templateGroupList.value = response.data.map(item=>{
      return {
        id: item.templateName,
        name: item.templateName
      }
    })
  })
}

function reset(){
  form.value = {}
  open.value = false
  title.value = ""
}

function handleUpdate(row){
  reset()
  getTemplate(row.id).then(response=>{
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
    updateTemplate(form.value).then(response=>{
      proxy.$modal.msgSuccess("修改成功")
      open.value = false
      getList()
    })
  } else {
    addTemplate(form.value).then(response=>{
      proxy.$modal.msgSuccess("新增成功")
      open.value = false
      getList()
    })
  }
}



function handleDelete(row){
  console.log(row)
  proxy.$modal
    .confirm('是否确认删除"' + row.fieldName + '"的数据项？')
    .then(function () {
      console.log(row.id)
      return delTemplate(row.id)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {
      console.log("删除失败")
    })
}

getTemplateGroupList()
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
