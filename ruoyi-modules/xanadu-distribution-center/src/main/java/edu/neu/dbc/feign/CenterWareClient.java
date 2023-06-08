package edu.neu.dbc.feign;


import edu.neu.dbc.vo.CenterInputVo;
import edu.neu.dbc.vo.CenterOutputVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "xanadu-ware", url = "/ware",contextId = "CenterWareClient")
@Component
public interface CenterWareClient {

    @PostMapping("/centerInput/feign/add")
    @ApiOperation("添加入库记录")
    public Boolean addInputRecord(@RequestBody CenterInputVo centerInputVo);

    @PostMapping("/centerOutput/feign/add")
    @ApiOperation("添加出库记录")
    public Boolean addOutputRecord(@RequestBody CenterOutputVo centerOutputVo);

}
