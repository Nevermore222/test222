<template>
<div>
  <el-table :data="tableData" stripe style="width: 100%" @row-dblclick="dataRow">
    <el-table-column prop="id" label="序号" width="180"> </el-table-column>
    <el-table-column prop="question" label="题目" width="280">
      
    </el-table-column>
    <el-table-column prop="answer" label="答案"> 
      <template slot-scope="scope">
          <el-form :model="scope.row">
            <el-form-item abel-width="160px" style="position: relative;">
              <el-input v-if="true" v-model="scope.row.answer" :type="password" style="width:100%;hight:100%"></el-input>
              <span v-else>{{scope.row.answer}}</span>
            </el-form-item>
          </el-form>
        </template>
    </el-table-column>
    <el-table-column label="隐藏答案" prop="" width="180" @click="hideAnswer"> </el-table-column>
  </el-table>
  <el-dialog v-drag :visible.sync="detailsdialog" title="详情">
	<div class="middle_modal_body">
		<el-form ref="details" :model="detailData" label-width="140px">
			<el-row>
				<el-col :span="12">
					<el-form-item label="题目" :show-overflow-tooltip="true">
					<el-input v-model="detailData.question" disabled style="width:100%;"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="回答" :show-overflow-tooltip="true">
					<el-input v-model="detailData.answer" disabled style="width:100%;"></el-input>
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
      detailsdialog: false,
      passw: 'password',
      tableData: [],
      detailData:[]
    };
  },
  mounted: function () {
    this.loadBooks();
  },
  methods: {
    loadBooks() {
      var _this = this;
      this.$axios.get("/review").then((resp) => {
        if (resp && resp.data.code === 200) {
          debugger;
          _this.tableData = resp.data.result;
          _this.tableData.isOK = true;
        }
      });
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
    hideAnswer(){
      
    }
  },
};
</script>