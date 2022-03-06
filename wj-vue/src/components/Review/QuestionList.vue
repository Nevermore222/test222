<template>
<div id="picture">
  <el-form>
    <el-form-item label="">
      <el-input prefix-icon="iconfont icon-sousuo" v-model="searchTableInfo" placeholder="请输入搜索内容" style="width:280px"></el-input>
        <el-select v-model="currentGroup" placeholder="分类" @change="this.loadBooks">
          <el-option  key="0" label="全部" :value="0" ></el-option>
          <el-option  :key="item" :label="item" :value="item" v-for="item in typeArray"></el-option>
        </el-select>
      </el-form-item>
  </el-form>
  <el-table 
    :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
    :show-header="true"
    :height="$store.state.clientHeight - 134"
    @selection-change="handleSelectionChange"
    style="width: 100%; overflow-y: auto;"
    stripe 
    @row-dblclick="dataRow">
    <el-table-column prop="type" label="类型" width="180"> </el-table-column>
    <el-table-column prop="question" label="题目" width="320">
      
    </el-table-column>
    <el-table-column prop="answer" label="答案" :show-overflow-tooltip="true"> 
      <template slot-scope="scope">
          <el-form :model="scope.row">
              <el-input :type="scope.row.showType" onmouseover="this.title=this.value" v-model="scope.row.answer" style="width:100%;"></el-input>
          </el-form>
        </template>
    </el-table-column>
    <el-table-column label="操作" width="180"> 
      <template slot-scope="scope">
        <el-button @click.native="hideAnswer(scope.row)" v-if="scope.row.isOK">显示答案</el-button>
        <el-button @click.native="hideAnswer(scope.row)" v-if="!scope.row.isOK">隐藏答案</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page="currentPage"
    :page-sizes="[6, 10, 20, 30, 50, 100]"
    :page-size="pageSize"
    layout="total, sizes, prev, pager, next, jumper"
    :total="tableData.length">
</el-pagination>
  <el-dialog :visible.sync="detailsdialog" title="详情" class="dialog">
	<div class="middle_modal_body">
		<el-form ref="details" :model="detailData" label-width="140px">
			<el-row> 
				<el-col :span="15" style="width:100%">
					<el-form-item label="题目">
					<el-input type="textarea" v-model="detailData.question" disabled style="width:100%;"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="15" style="width:100%">
					<el-form-item label="回答">
					<el-input type="textarea" onmouseover="this.title=this.value" v-model="detailData.answer"  style="width:100%;" ></el-input>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
	</div>
</el-dialog>

</div>
</template>

<script>
export default {
data() {
  return {
    //搜索条件
    searchTableInfo:"",
    detailsdialog: false,
    detailData:[],
    // 当前页
    currentPage: 1,
    // 每页多少条
    pageSize: 6,
    getSearchInfo:[],
    typeArray:[],
    currentGroup:''
  };
},
// mounted: function () {
//   this.loadBooks();
//   this.getSearchInfo = tableData;
//   console.log(this.getSearchInfo)
// },
computed: {
    // 根据计算属性模糊搜索
    tableData () {
      const searchTableInfo = this.searchTableInfo
      if (searchTableInfo) {
        // filter() 方法创建一个新的数组，新数组中的元素是通过检查指定数组中符合条件的所有元素。
        // 注意： filter() 不会对空数组进行检测。
        // 注意： filter() 不会改变原始数组。
        return this.getSearchInfo.filter(data => {
          console.log("success"+data)
          // some() 方法用于检测数组中的元素是否满足指定条件;
          // some() 方法会依次执行数组的每个元素：
          // 如果有一个元素满足条件，则表达式返回true , 剩余的元素不会再执行检测;
          // 如果没有满足条件的元素，则返回false。
          // 注意： some() 不会对空数组进行检测。
          // 注意： some() 不会改变原始数组。
          return Object.keys(data).some(key => {
            // indexOf() 返回某个指定的字符在某个字符串中首次出现的位置，如果没有找到就返回-1；
            // 该方法对大小写敏感！所以之前需要toLowerCase()方法将所有查询到内容变为小写。
            return String(data[key]).toLowerCase().indexOf(searchTableInfo) > -1
          })
        })
      }
      return this.getSearchInfo
    }
  },
  methods: {
    loadBooks() {
      var _this = this;
      let url='';
      if(_this.currentGroup==0 || _this.currentGroup === ""){
          url='review/'+'0'+'/ooo';
      }else{
          url= 'review/'+_this.currentGroup+'/ooo';
      }
      this.$axios.get(url).then((resp) => {
        if (resp && resp.data.code === 200) {
          _this.getSearchInfo = resp.data.result;
          this.loadTypes();
        }
      });
    },
    loadTypes(){
      var _this = this;
      this.$axios.get("/reviewType").then((resp) => {
        if (resp && resp.data.code === 200) {
          _this.typeArray = resp.data.result;
        }
      });
    },
    getAllType(){
      let that=this;
      let url='';
      if(that.currentGroup==0){
          url='review';
      }else{
          url= '/reviewByType?type='+that.currentGroup
      }
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },
    dataRow(row,event,column){
      //详情detail
      this.detailsdialog = true;
      //赋值
      this.detailData = row;
    },
    hideAnswer(row){
      //更改答案的type属性
      if(row.isOK){
        row.showType = 'textarea';
      }else{
        row.showType = 'password';
      }
      row.isOK = !row.isOK
    },
    // 每页多少条
    handleSizeChange(val) {
        this.pageSize = val;
    },
    // 当前页
    handleCurrentChange(val) {
        this.currentPage = val;
    },
    //操作多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    }
  },
    created(){
        this.loadBooks();
    }
};
</script>
<style scoped>
  #picture {
    background: url("../../assets/img/review/bc2.jpeg") repeat;
  }
  .dialog {
    background: url("../../assets/img/review/bc2.jpeg") repeat;
  }
  .el-table .cell, .el-table th div {
	padding-right: 0;
}

</style>