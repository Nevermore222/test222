<template>
  <div class="row analysis-content">
    <div class="analysis-content-head">
      <h3>右侧显示区</h3>
    </div>
 
    <div class="analysis-content-body">
      <div class="upload-tool">
        <span>
          <input class="input-file" type="file" @change="exportData" 
                 accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" />
          <el-button type="success" size="small" @click="btnClick">上传excel</el-button>
        </span>
 
        <el-button type="primary" @click="openColumnDrawer" size="small">查看显示列</el-button>
      </div>
 
 
      <div class="table-area">
          <div>
              <el-table
                :data="tableData"
                style="width: 100%"
                 height="450"
                 size="mini">
                <el-table-column
                  :prop="item"
                  :label="item"
                  width="140"
                  v-for="(item,index) in checkboxTableColumn"
                  :key="'column'+index">
                </el-table-column>
              </el-table>
 
              <div class="pagination">
                <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="CurrentChange"
                  :current-page="currentPage"
                  :page-sizes="[10, 20, 30,40,50,100,200,500]"
                  :page-size="pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="total">
                </el-pagination>
 
              </div>
          </div>
      </div>
 
      <el-drawer
        :before-close="closeColumnDrawer"
        :visible.sync="columnDrawerVisible"
        direction="rtl"
        :withHeader=false
        custom-class="column-drawer"
        ref="columnDrawer"
        size="40%">
      <div class="drawer-content" style="overflow:auto;width:100%;background:#fff;">
        <div class="column-content">
            
            <!-- 表格所有列 -->
            <el-checkbox-group v-model="checkboxTableColumn" size="mini">
              <el-checkbox :label="item" border v-for="(item,index) in columnHeader" :key="'column'+index"></el-checkbox>
            </el-checkbox-group>
 
        </div>
        <div class="drawer-footer" style="">
          <el-button @click="closeColumnDrawer" size="small">取 消</el-button>
        </div>
      </div>
    </el-drawer>
 
    </div>
  </div>
</template>
 
<script>
import * as XLSX from 'xlsx/xlsx.mjs'
 
export default {
  components: {
   
  },
  data() {
    return {
        //所有键的值
        columnHeader:[],
        // 导入的excel的数据
        excelData:[],
 
        // 表格显示列
        checkboxTableColumn:[],
        //表格数据
        tableData:[],
        // 当前分页
        currentPage:1,
        // 每页显示数量
        pageSize:10,
        // 数据总数
        total:0,
 
 
        // 权限抽屉显示
        columnDrawerVisible:false,
        
    };
  },
  computed:{
     
  },
  updated(){
      
  
  },
  methods: {
      btnClick() {
        document.querySelector(".input-file").click();
      },
      exportData(event) {
        debugger
        if(!event.currentTarget.files.length) {
          return;
        }
        const that = this;
        // 拿取文件对象
        let f = event.currentTarget.files[0];
        this.dialogFileInfo(event);

        //这里已经拿到了excel的file文件，若是项目需求，可直接$emit丢出文件
        // that.$emit('getMyExcelData',f);
        // 用FileReader来读取
        let reader = new FileReader();
        // 重写FileReader上的readAsBinaryString方法
        // FileReader.prototype.readAsBinaryString = function(f) {
        //   let binary = "";
        //   let wb; // 读取完成的数据
        //   let outdata; // 你需要的数据
        //   let reader = new FileReader();
          reader.onload = function(e) {
            // 读取成Uint8Array，再转换为Unicode编码（Unicode占两个字节）
            // let bytes = new Uint8Array(reader.result);
            // let length = bytes.byteLength;
            // for (let i = 0; i < length; i++) {
            //   binary += String.fromCharCode(bytes[i]);
            // }
            var data = e.target.result;
            // 接下来就是xlsx
            var wb = XLSX.read(data, {
              type: "buffer"
            });
            var outdata = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]);
            // 导出格式为json，{表数据：[]}
            console.log(outdata);
            that.excelData=outdata;
            that.tableData=outdata.slice(0,10);
            that.total=outdata.length;
 
 
            // 获取数据的key
            var importDataThead = Array.from(Object.keys(outdata[0])).map(
              item => {
                return item;
              }
            );
 
            that.columnHeader=importDataThead;
            that.checkboxTableColumn=importDataThead.slice(0,9);
            console.log(importDataThead);
          };
          reader.readAsArrayBuffer(f);
        // };
        // reader.readAsBinaryString(f);
      },

      dialogFileInfo(event){//文件上传到服务器
            this.dialogFile = event.currentTarget.files[0];
            if(this.dialogFile==null||this.dialogFile==""){
                this.$message({type:"warning",message: "请先选择文件上传！"});
                return;
            }
            var files=new FormData();
            files.append("diaFile",this.dialogFile);//单个文件上传
            console.log(this.dialogFile.original);
              this.$axios({
                  url:process.env.API_ROOT+'/ExcelInsertGoodinStore',
                  method:"post",
                  data:files,
              }).then(data=>{
                debugger
                  //this.isDisplay.files=false;
                  // var obj = {person: this.realname, words: "文件："+this.dialogFileName+"-上传成功！！！"};
                  // this.arr.push(obj);
                  //this.$websocket.state.websock.send(this.userName+"#$文件:"+this.dialogFileName+"#"+this.receiveUserName);
                  this.$message({type:"success",message: "文件上传成功！！！"});
              }).catch(error=>{
                  this.$message("连接超时！");
              })
          },
       // 分页切换
      CurrentChange(val){
        this.currentPage=val;
        
        this.tableData=this.excelData.slice((val-1)*this.pageSize,(val*this.pageSize)-1);
      },
      // 每页显示数量改变
      handleSizeChange(val){
 
        this.pageSize=val;
        this.tableData=this.excelData.slice((this.currentPage-1)*val,(this.currentPage*val)-1);
        
      },
 
      // 打开表格先显示列
      openColumnDrawer(){
          this.columnDrawerVisible=true;
      },
      // 关闭表格所有列的显示
      closeColumnDrawer(){
        this.columnDrawerVisible=false;
      }
  }
};
</script>
 
<style>
  .analysis-content{
    box-sizing: border-box;
    background: #efefef;
    padding:15px;
  }
  
  .analysis-content-head{
    box-sizing: border-box;
    background: #fff;
    padding:15px;
  }
 
  .analysis-content-body{
    box-sizing: border-box;
    margin-top: 15px;
    background: #fff;
    padding:15px;
    height:550px;
  }
 
  .input-file {
    display: none;
  }
 
  .column-content{
    box-sizing:border-box;padding:40px 15px 60px;
   
 
  }
  .el-checkbox-group{
     display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: flex-start;
  }
  .el-checkbox.is-bordered{
    margin-bottom:15px;
  }
  .el-checkbox.is-bordered+.el-checkbox.is-bordered {
    margin-left: 0;
  }
 
 
.drawer-footer{
   text-align: center;
}
</style>
 