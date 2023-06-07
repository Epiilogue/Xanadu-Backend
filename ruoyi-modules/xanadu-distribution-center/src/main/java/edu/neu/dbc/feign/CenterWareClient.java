package edu.neu.dbc.feign;


import edu.neu.dbc.vo.CenterInputVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "xanadu-ware",url = "/ware/centerInput")
public interface CenterWareClient {

    @PostMapping("/feign/add")
    @ApiOperation("添加入库记录")
    public Boolean add(@RequestBody CenterInputVo centerInputVo);


}
