package edu.neu.dpc.feign;

import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dpc.vo.StorageVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "xanadu-ware", contextId = "centerWareClient")
public interface CenterWareClient {

    @PutMapping("/ware/centerStorageRecord/feign/dispatch/{productId}/{num}/{from}")
    @ApiOperation("分配商品库存,将商品库存由锁定的数量添加到已分配的数量中")
    public AjaxResult dispatch(@PathVariable("productId") Long productId, @PathVariable("num") Integer num, @PathVariable("from")String from);

    @PutMapping("/ware/centerStorageRecord//feign/unlock/{productId}/{num}")
    @ApiOperation("解锁商品库存")
    public Boolean unlock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num);


    @GetMapping("/feign/getStorage/{productId}")
    @ApiOperation("获取商品各类库存信息")
    public StorageVo getStorage(@PathVariable("productId") Long productId);
}
