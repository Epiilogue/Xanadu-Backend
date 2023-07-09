package edu.neu.dpc.feign;

import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dpc.vo.CenterOutputVo;
import edu.neu.dpc.vo.StorageVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "xanadu-ware", contextId = "centerWareClient")
public interface CenterWareClient {

    @PutMapping("/ware/centerStorageRecord/feign/dispatch/{productId}/{num}/{from}")
    @ApiOperation("分配商品库存,将商品库存由锁定的数量添加到已分配的数量中")
    public AjaxResult dispatch(@PathVariable("productId") Long productId, @PathVariable("num") Integer num, @PathVariable("from")String from);

    @PutMapping("/ware/centerStorageRecord/feign/unlock/{productId}/{num}")
    @ApiOperation("解锁商品库存")
    public Boolean unlock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num);


    @GetMapping("/ware/centerStorageRecord/feign/getStorage/{productId}")
    @ApiOperation("获取商品各类库存信息")
    public StorageVo getStorage(@PathVariable("productId") Long productId);


    @PutMapping("/ware/centerStorageRecord/feign/reDispatch/{productId}/{prevNum}/{nowNum}")
    @ApiOperation("回滚商品分配库存")
    AjaxResult reDispatch(@PathVariable("productId") Long productId, @PathVariable("prevNum") Integer prevNum, @PathVariable("nowNum") Integer nowNum);


    @PostMapping("/ware/centerOutput/feign/add")
    @ApiOperation("添加出库记录")
    public Boolean add(@RequestBody CenterOutputVo centerOutputVo);


    @DeleteMapping ("/ware/centerOutput/feign/delete")
    @ApiOperation("删除出库记录")
    public Boolean delete(@RequestParam("outputId") Long outputId);

    @PostMapping("/ware/centerOutput/feign/update")
    @ApiOperation("修改出库记录")
    public AjaxResult update(@RequestBody CenterOutputVo centerOutputVo);



    @GetMapping("/ware/centerware/info")
    @ApiOperation("获取仓库信息")
    public AjaxResult info();

}
