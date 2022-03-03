<template>
      
  <div>
           <UploadExcelComponent
      :on-success="handleSuccess"
      :before-upload="beforeUpload"
    />
        
  </div>
</template>
<script>
import UploadExcelComponent from "./UploadExcel.vue";

export default {
  name: "UploadExcel",
  components: { UploadExcelComponent },
  data() {
    return {
      tableData: [],
      tableHeader: [],
    };
  },
  methods: {
    beforeUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1;
      if (isLt1M) {
        return true;
      }
      this.$message({
        message: "Please do not upload files larger than 1m in size.",
        type: "warning",
      });
      return false;
    },

    handleSuccess({ results, header }) {
      this.tableData = results;
      this.tableHeader = header;
    },
  },
};
</script>
<style scoped>
</style>